pipeline {
    agent {
        node {
            label 'docker-mvn'
        }
    }

    environment {
        COLLECTION_FILE = 'collection.json'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Unit Tests') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

        stage('Run API in Test Mode') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'firebase-key', variable: 'FIREBASE_KEY_FILE')]) {
                        sh 'cp $FIREBASE_KEY_FILE firebase-private-key.json'
                        sh """
                            cp firebase-private-key.json file/src/main/resources/firebase-private-key.json
                            docker compose -f .\\docker-compose-staging.yaml up
                        """
                    }
                }
            }
        }

        stage('Run Newman Tests') {
            steps {
                script {
                    retry(5) {
                        sleep(15)
                        sh "curl -f http://localhost:8080/health || exit 1"
                    }

                    sh '''
                        newman run $COLLECTION_FILE \
                        --reporters cli,junit --reporter-junit-export newman-results.xml
                    '''
                }
            }
        }

        stage('Stop Test API Container') {
            steps {
                script {
                    sh 'docker compose -f .\\docker-compose-staging.yaml down'
                }
            }
        }
    }

//     post {
//         always {
//             // cleanWs()
//         }
//     }
}
