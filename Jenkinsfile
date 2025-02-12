pipeline {
    agent {
        node {
            label 'nodejs-newman-jdk21-mvn'
        }
    }
    environment {
        COLLECTION_FILE = 'collection.json'
        REMOTE_SERVER_IP = '192.168.100.135'
        REMOTE_USER = 'Cristian'
        APP_SERVER_PATH = "C:/Users/Cristian/Desktop/cicd/ssh"
        EMAIL_RECIPIENT = 'cristianh9073@gmail.com'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        //         stage('Ejecutar Pruebas Unitarias') {
        //             steps {
        //                 script {
        //                     sh 'mvn test'
        //                 }
        //             }
        //         }
        stage ('Limpiar Carpeta de Aplicación en Servidor de Staging') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                        sh """
                            ssh \
                                -i ${SSH_PRIVATE_KEY} \
                                -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER_IP} \
                                'rd /s /q  ${APP_SERVER_PATH}/ || exit 0 && mkdir ${APP_SERVER_PATH}'
                        """
                    }
                }
            }
        }
        stage('Copiar Archivos al Servidor de Staging') {
            steps {
                script {
                    withCredentials([
                        file(credentialsId: 'firebase-key', variable: 'FIREBASE_KEY_FILE'),
                        file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')
                    ]) {
                        sh """
                            mkdir -p file/src/main/resources
                            cp ${FIREBASE_KEY_FILE} file/src/main/resources/firebase-private-key.json
                            scp -i ${SSH_PRIVATE_KEY} \
                                -o StrictHostKeyChecking=no \
                                -r ./ ${REMOTE_USER}@${REMOTE_SERVER_IP}:${APP_SERVER_PATH}
                        """
                    }
                }
            }
        }
        stage('Levantar API en Modo de Prueba') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                        sh """
                            ssh -i ${SSH_PRIVATE_KEY} \
                                -o StrictHostKeyChecking=no \
                                ${REMOTE_USER}@${REMOTE_SERVER_IP} \
                                'docker compose -f ${APP_SERVER_PATH}/docker-compose-staging.yaml down --rmi local && \
                                docker compose -f ${APP_SERVER_PATH}/docker-compose-staging.yaml up -d'
                        """
                    }
                }
            }
        }
        stage('Ejecutar Pruebas de Newman') {
            steps {
                script {
                    retry(5) {
                        sleep(15)
                        sh "curl -f http://${REMOTE_SERVER_IP}:7937/health || exit 1"
                    }
                    sh '''
                        rm -f report.html
                        newman run $COLLECTION_FILE --env-var hostURL=http://${REMOTE_SERVER_IP}:7937 \
                            -r html --reporter-html-export report.html
                    '''
                }
            }
        }
        stage('Ejecutar Pruebas de JMeter') {
            steps {
                script {
                    sh '''
                        jmeter -n -t jmeter.jmx -Jhost="${REMOTE_SERVER_IP}" -Jport=6211 -l jmeter.jtl
                        ls -altr
                    '''
                }
            }
        }
    }
    post {
        always {
            script {
                emailext (
                    subject: "Newman Test Report",
                    body: "Aquí está el reporte de las pruebas de Newman.",
                    attachmentsPattern: "**/report.html,**/jmeter.jtl",
                    to: "${EMAIL_RECIPIENT}"
                )
            }
            // Detener API en Servidor de Staging
            script {
                withCredentials([file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')]) {
                    sh """
                        ssh \
                            -i ${SSH_PRIVATE_KEY} \
                            -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_SERVER_IP} \
                            'docker compose -f ${APP_SERVER_PATH}/docker-compose-staging.yaml down --rmi local'
                    """
                }
            }
        }
    }
}
