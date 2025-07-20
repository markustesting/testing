pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/markustesting/testing.git'  // Replace with your repo URL
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'mvn test -Dtest=com.markus.automation.api.test.JsonPlaceholderApiTest'
            }
        }
    }
}
