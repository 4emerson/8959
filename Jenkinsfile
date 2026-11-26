pipeline {
    agent any

    options{
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    environment {
        IMAGE_NAME = 'course-catalog'
        IMAGE_TAG  = "0.${BUILD_NUMBER}"
    }

    stages{
      stage('Testes unitarios') {
        steps {
          sh 'docker build --target test -t ${IMAGE_NAME}:test-${IMAGE_TAG} .'
          sh 'docker run --name unit-${IMAGE_TAG} ${IMAGE_NAME}:test-${IMAGE_TAG}'
        }
        post {
          always {
            sh '''
              rm -rf reports
              docker cp unit-${IMAGE_TAG}:/courseCatalog/reports . || true
              docker rm -f unit-${IMAGE_TAG} || true
              # o relatorio de cobertura aponta para /courseCatalog, o caminho dentro do container
              sed -i "s|/courseCatalog|${WORKSPACE}|g" reports/coverage.xml || true
            '''
            junit allowEmptyResults: true, testResults: 'reports/junit.xml'
            archiveArtifacts artifacts: 'reports/**', allowEmptyArchive: true
          }
        }
      }
    }
}