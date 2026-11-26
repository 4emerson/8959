node {
    stage('Variaveis e condicionais'){
        def ambientes = ['dev', 'homolog', 'production']
        for (amb in ambientes){
            if ( amb == 'production'){
                echo "${amb}: exige aprovação"
            } else {
                 echo "${amb}: deploy automatico"
            }
        }
    }
    stage('Tratando erros'){
        try {
            sh 'exit 1'
        } catch (err) {
            echo "O comando falhou: ${err}"
            currentBuild.result = 'UNSTABLE'
        } finally {
            echo 'O finally sempre executa'
        }
    }
}