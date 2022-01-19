pipeline {
    agent any
    tools {
        maven 'Maven 3.8.1'
        jdk 'jdk8'
    }
    stages {
        stage ('Build') {
            steps {
                sh ' mvn clean install -DskipTests'
            }
        }
        stage ('run app') {
            steps {
                sh 'java -jar -Xmx512M target/integra-anomalia-0.0.1-SNAPSHOT.jar'
            }
        }

    }
}
