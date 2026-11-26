pipeline {
    agent any

    environment {
        APP_NAME = "supermario-${BUILD_NUMBER}"
        APP_PORT = '8600'
    }

    stages{
        stage('Subinod com o docker run'){
            steps{
                sh '''
                   docker run -d --name ${APP_NAME} -p ${APP_PORT}:8080 pengbai/docker-supermario
                   docker ps --filter name=${APP_NAME}

                '''
            }
        }

        stage('Testando a aplicação') {
            steps {
              timeout(time: 2, unit: 'MINUTES'){
                sh 'until curl -fs http://localhost:${APP_PORT} > /dev/null; do sleep 3; done'
            }
            sh 'curl -s -o /dev/null -w "HTTP %{http_code}\\n"  http://localhost:${APP_PORT}/'

            }
        }

        stage('Bora jogar'){
            steps{
                echo "Acesse http://192.168.88.10:${APP_PORT}"
                timeout(time: 10, unit: 'MINUTES'){
                input message: 'Terminou de jogar? A aplicação sera removida', ok: 'Remover'
                }
            }
        }
    }
    post {
        always {
            sh 'docker logs --tail 20 ${APP_NAME} || true'
            sh 'docker rm -f ${APP_NAME} || true'
        }
    }
}