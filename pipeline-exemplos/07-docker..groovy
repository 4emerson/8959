pipeline {
    agent any

    stages {
        stage('Agent Docker'){
            agent{
                docker { image 'python:3.12-slim'}
            }
            steps {
                sh 'python3 --version'
                sh ' cat /etc/os-release | head -1'
            }
        }
        stage('Outra imagem'){
            agent {
                docker { image 'alpine:3.20'}
            }
            steps{
                sh ' cat /etc/os-release | head -1'
            }
        }
        stage('docker.image().inside') {
            agent any
            steps {
                script{
                    docker.image('python:3.12-slim').inside{
                        sh 'python3 -c "print(2+2)"'
                    }
                }
            }
        }

    }
}