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
        stage('Run API in Test Mode') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'firebase-key', variable: 'FIREBASE_KEY_FILE')]) {
                        echo 'find ./file/src/'
                        sh 'cp $FIREBASE_KEY_FILE file/src/main/resources/firebase-private-key.json'
                        sh """
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