pipeline{
    agent any
    
    stages{
        stage('Variaveis de Ambiente'){
            steps{
                sh 'printenv | sort'
            }
        }
    }
    post{
        always {
            echo "Pipeline Finalizada"
        }
        success{
            echo "Pipeline finalizada com sucesso"
        }
        failure{
            echo "Pipeline finalizada com Falha"
        }
    }
}
