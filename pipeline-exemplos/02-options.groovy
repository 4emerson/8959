pipeline {
    agent any
    
    options{
        timestamps()
        timeout(time: 1, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages{
        stage('Rapido'){
            steps{
                sh 'echo inicio; sleep 2; echo fim'
            }
        }
        stage('Lento'){
            steps{
                sh 'sleep 90'
            }
        }
    }
    post{
        aborted {
            echo "Pipeline interrompida pelo timeout"
        }
    }
}   
