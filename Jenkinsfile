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
            	sh 'echo nexus63987 | sudo -S pkill -9 -f integra-anomalia-0.0.1-SNAPSHOT.jar'
                sh 'java -jar -Xmx512M target/integra-anomalia-0.0.1-SNAPSHOT.jar'
            }
        }

    }
}
