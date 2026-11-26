pipeline {
    agent any

    stages{
        stage('Com withCredentials'){
            steps{
                withCredentials([usernamePassword(credentialsId: 'nexus-docker', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]){
                    sh 'echo Usuário: $NEXUS_USER'
                    sh 'echo "A senha do nexus tem ${#NEXUS_PASSWORD} caracteres"'
                }
            }
        }
    }
}