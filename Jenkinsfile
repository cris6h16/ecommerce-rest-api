pipeline {
    agent {
        node {
            label 'nodejs-newman-jdk21-mvn'
        }
    }
    environment {
        COLLECTION_FILE = 'collection.json'
        REMOTE_SERVER_IP = '192.168.19.79'
        REMOTE_USER = 'Cristian'
        APP_SERVER_PATH = "C:\\Users\\Cristian\\Desktop\\cicd\\ssh"
        EMAIL_RECIPIENT = 'cristianh9073@gmail.com'
    }
    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                checkout scm
                sh 'find .'
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
                                'rd /s /q  ${APP_SERVER_PATH}  && mkdir ${APP_SERVER_PATH}'
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
                                -r ./ ${REMOTE_USER}@${REMOTE_SERVER_IP}:"${APP_SERVER_PATH}"
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
                                'docker compose -f ${APP_SERVER_PATH}\\docker-compose-staging.yaml down --rmi local && \
                                docker compose -f ${APP_SERVER_PATH}\\docker-compose-staging.yaml up -d'
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
                    def jmeterCsvPath = sh(script: 'realpath jmeter.csv', returnStdout: true).trim()
                    sh """
                       jmeter -n -t jmeter.jmx -Jhost="${REMOTE_SERVER_IP}" -Jport=7937 -JjmeterCsv="${jmeterCsvPath}" -l jmeter.jtl
                   """
                }
            }
        }
    }
    post {
        always {
            script {
                def subject = (currentBuild.result == 'FAILURE') ? "❌ PIPELINE FALLIDO | Jenkins": "✅ PIPELINE EXITOSO | Jenkins"
                def body = (currentBuild.result == 'FAILURE') ? "🚨 El pipeline falló. Revisa los reportes adjuntos. (si estan presentes)":
                "🎉 Pipeline se completo con éxito. Revisa los reportes adjuntos. (si estan presentes)"
                emailext (
                    subject: subject,
                    body: body,
                    attachmentsPattern: "**/report.html,**/jmeter.jtl, **/jmeter.log",
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
                            'docker compose -f ${APP_SERVER_PATH}\\docker-compose-staging.yaml down --rmi local'
                    """
                }
            }
            script {
                withCredentials([
                    file(credentialsId: 'win-private-key', variable: 'SSH_PRIVATE_KEY')
                ]) {
                    sh """
                        scp -i ${SSH_PRIVATE_KEY} -o StrictHostKeyChecking=no report.html jmeter.jtl jmeter.log ${REMOTE_USER}@${REMOTE_SERVER_IP}:"C:\Users\Cristian\Desktop"
                        """
                }
            }
        }
    }
}
