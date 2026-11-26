pipeline{
    agent any
    
    environment{
        DIR_REPO = 'repo'
        IMAGE_NAME = 'course-catalog'
        IMAGE_TAG = "0.${BUILD_NUMBER}"
        CONTAINER_IMAGE = "${IMAGE_NAME}:${IMAGE_TAG}"
    }

    stages{
        stage('Variaveis do pipeline'){
            steps{
                echo "DIR_REPO = ${DIR_REPO}"
                echo "CONTAINER_IMAGE = ${env.CONTAINER_IMAGE}"
                sh 'echo "no shell: $CONTAINER_IMAGE"'
            }
        }

        stage('Variaveis do Jenkins'){
            steps{
                echo "Job ${env.JOB_NAME}, execucao ${env.BUILD_NUMBER}"
                echo "Workspace: ${env.WORKSPACE}"
                echo "URL: ${env.BUILD_URL}"
            }
        }
        stage('Variavel do stage'){
            environment{
                AMBIENTE = 'homolog'
            }
            steps{
                sh 'echo "Imagem $CONTAINER_IMAGE no ambiente $AMBIENTE"'               
            }           
        }
        stage('Aspas simples e duplas'){
            steps{
                sh "echo 'Groovy interolou: ${IMAGE_TAG}'"
                sh 'echo "o shell interpolou: ${IMAGE_TAG}"'
            }

        }
    }
}

        