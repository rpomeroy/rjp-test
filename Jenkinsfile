pipeline {
  agent { docker: 'openjdk:8u121-jdk-alpine' }
  stages {
    stage("Build") {
      steps {
        sh 'mvnw clean install -Dmaven.test.failure.ignore=true'
    }
    stage("Archive"){
      steps {
        archive "*/target/**/*"
        junit '*/target/surefire-reports/*.xml'
      }
    }
  }
  post {
    always {
      deleteDir()
    }
    success {
      echo 'Success'
    }
    failure {
      echo 'Failure'
    }
  }
}