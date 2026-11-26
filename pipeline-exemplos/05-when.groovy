pipeline {
    agent any

    parameters{
            choice(name: 'ENVIRONMENT', choices: ['DEV', 'HOMOLOG', 'PRODUCTION'], description: 'Ambiente')
            booleanParam(name: 'CRIAR', defaultValue: true, description: 'Criar ou não o arquivo')
        }
    
    stages {
        stage('Somente se CRIAR'){
            when {
                expression { params.CRIAR}
            }
            steps {
                echo 'Criar marcado'
            }
        }
        stage('Somente PRODUCTION'){
            when{
                allOf {
                    expression { params.CRIAR}
                    environment name: 'ENVIRONMENT', value: 'PRODUCTION'
                }
            }
            steps {
                echo 'CRIAR marcado e ambiente PRODUCTION'
            }
        }
        stage('HOMOLOG ou PRODUCTION'){
            when{
                anyOf {
                    environment name: 'ENVIRONMENT', value: 'HOMOLOG'
                    environment name: 'ENVIRONMENT', value: 'PRODUCTION'
                }
            }
            steps {
                echo "Ambiente: ${params.ENVIRONMENT}"
            }
        }
        stage('Somente na branch main'){
            when {
                branch 'main'
            }
            steps {
                echo 'Executando somente na main'
            }
        }
    }

}