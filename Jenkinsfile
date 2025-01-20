pipeline {
    agent {
        node {
            label 'docker-mvn'
        }
    }

    environment {
        DOCKER_IMAGE = 'ecommerce-rest-api'
        DOCKER_TAG = "${BUILD_NUMBER}"
        COLLECTION_FILE = 'collection.json'
        PROD_PROFILE = 'prod'
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

//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     sh """
//                         docker build -t $DOCKER_IMAGE:$DOCKER_TAG .
//                     """
//                 }
//             }
//         }

        stage('Run API in Test Mode') {
            steps {
                script {
                    sh """
                        docker compose -f .\docker-compose-staging.yaml up
                    """
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
                    sh 'docker compose -f .\docker-compose-staging.yaml down'
                }
            }
        }

//         stage('Deploy API in Production Mode') {
//             steps {
//                 script {
//                     sh """
//                         docker stop ecommerce-api || true
//                         docker rm ecommerce-api || true
//
//                         nohup docker run -d --name ecommerce-api -p 8080:8080 \
//                         -e SPRING_PROFILES_ACTIVE=$PROD_PROFILE $DOCKER_IMAGE:$DOCKER_TAG &
//                     """
//                 }
//             }
//         }
    }

    post {
        always {
//             cleanWs()
        }
    }
}
