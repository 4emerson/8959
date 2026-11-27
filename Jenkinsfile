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
        TRIVY_VERSION = '0.75.0'

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
      stage('SonarQube'){
        agent {
          docker {
            image 'sonarsource/sonar-scanner-cli:latest'
            args '--entrypoint= -e SONAR_USER_HOME=/tmp/.sonar'
            reuseNode true
          }
        }
        steps {
          withSonarQubeEnv('SonarQube') {
            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
              sh '''
                sonar-scanner \
                  -Dsonar.host.url=${SONAR_HOST_URL} \
                  -Dsonar.projectKey=course-catalog \
                  -Dsonar.sources=project \
                  -Dsonar.tests=tests \
                  -Dsonar.python.version=3.12 \
                  -Dsonar.python.coverage.reportPaths=reports/coverage.xml \
                  -Dsonar.python.xunit.reportPath=reports/junit.xml
              '''
            }
          }
        }        
      }
      stage('Build'){
        steps{
          sh 'docker build --build-arg APP_VERSION=${IMAGE_TAG} -t ${IMAGE_NAME} .'
        }
      }

      stage('Trivy') {
        steps {
          sh '''
            docker run --rm \
              -v /var/run/docker.sock:/var/run/docker.sock \
              -v trivy-cache:/root/.cache/trivy \
              aquasec/trivy:${TRIVY_VERSION} image \
              --severity CRITICAL --ignore-unfixed \
              --timeout 30m ${IMAGE_NAME}
          '''
        }
      }
    }
}
