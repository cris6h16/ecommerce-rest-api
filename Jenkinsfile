pipeline {
    agent {
        node {
            label 'docker-mvn'
        }
    }
    environment {
        COLLECTION_FILE = 'collection.json'
        REMOTE_SERVER = '192.168.100.135'
        REMOTE_USER = 'Cristian'
        APP_SERVER_PATH = "C:/Users/Cristian/Desktop/cicd/ssh"
        EMAIL_RECIPIENT = 'cristianmherrera21@gmail.com'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage ('Limpiar Carpeta') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                        sh """
                            ssh -i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER} 'rd /s /q  ${APP_SERVER_PATH}/ || exit 0'
                        """
                        ssh "-i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER} 'mkdir ${APP_SERVER_PATH}'"
                    }
                }
            }
        }
        stage('Copy Application to Remote') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'firebase-key', variable: 'FIREBASE_KEY_FILE'), file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                        sh 'mkdir -p file/src/main/resources'
                        sh 'cp ${FIREBASE_KEY_FILE} file/src/main/resources/firebase-private-key.json'
                        sh """
                           scp -i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no -r ./ ${REMOTE_USER}@${REMOTE_SERVER}:${APP_SERVER_PATH}
                        """
                    }
                }
            }
        }
        stage('Run API in Test Mode') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                        sh """
                            ssh -i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER} 'docker compose -f ${APP_SERVER_PATH}/docker-compose-staging.yaml down --rmi local'
                            ssh -i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER} 'docker compose -f ${APP_SERVER_PATH}/docker-compose-staging.yaml up -d'
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
                        sh "curl -f http://${REMOTE_SERVER}:7937/health || exit 1"
                    }
                    sh 'rm -f report.html'
//                         newman run $COLLECTION_FILE --env-var hostURL=http://${REMOTE_SERVER}:7937 -r html --reporter-html-export report.html
                    sh '''
                        npm install -g newman newman-reporter-html
                        newman run $COLLECTION_FILE --env-var hostURL=http://${REMOTE_SERVER}:7937
                    '''
                }
            }
        }
        stage('Stop Test API Container') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                        sh """
                            ssh -i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER} 'docker compose -f ${APP_SERVER_PATH}/docker-compose-staging.yaml down --rmi local'
                        """
                    }
                }
            }
        }
    }
    post {
        always {
            emailext (
                subject: 'Newman Test Report',
                body: 'Aquí está el reporte de las pruebas de Newman.',
                attachmentsPattern: '**/report.html',
                to: "${EMAIL_RECIPIENT}"
            )
        }
    }
}
