pipeline {
  agent {
    docker {
      image 'openjdk:8u121-jdk-alpine'
    }
  }
  stages {
    stage('build') {
      steps {
        sh './mvnw clean package'
      }
    }
  }
}