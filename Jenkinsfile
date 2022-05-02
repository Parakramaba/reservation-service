pipeline {
    agent any
    stages {
        stage('Scan') {
            steps {
                withSonarQubeEnv('sonarq1') {
                    bat 'mvn clean package sonar:sonar'
                }
            }
        }
    }
}