pipeline {
    agent any

    parameters{
        choice(name: 'ENVIRONMENT', choices: ['DEV', 'HOMOLOG', 'PRODUCTION'], description: 'Defina um ambiente para executar o pipeline')
        string(name: 'ARQUIVO', defaultValue: 'arquivo.txt', description: 'Nome do arquivo a se criar')
        text(name: 'CONTEUDO', defaultValue: 'Primeira Linha', description: 'Conteudo do arquivo')
        booleanParam(name: 'CRIAR', defaultValue: true, description: 'Criar ou não o arquivo')
        password(name: 'SENHA', defaultValue: 'qwe123qwe', description: 'Uma senha de exemplo')
    }

    stages{
        stage('Parametros'){
            steps{
                echo "Ambiente: ${params.ENVIRONMENT}"
                echo "Arquivo a se criar: ${params.ARQUIVO}"
                echo "Criar? ${params.CRIAR}"
                sh 'echo "A SENHA tem ${#SENHA} caracters"'
            }
        }
        stage('Criando o arquivo'){
            when {
                expression { params.CRIAR }
            }
            steps {
                writeFile file: params.ARQUIVO, text: params.CONTEUDO
                sh "cat ${params.ARQUIVO}"
            }
        }
    }
}