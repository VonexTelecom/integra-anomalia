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
        stage ('Imagem docker') {
            steps {
                sh 'docker build . -t vonex/integra_anomalia:${BUILD_NUMBER}'
            }
        }
        stage ('Run docker') {
            steps {
                sh ' docker-compose stop integra-anomalia || true '
                sh ' docker-compose rm integra-anomalia || true '
                sh ' BUILD_NUMBER=${BUILD_NUMBER} docker-compose -f docker-compose.yml up --scale integra-anomalia=2 -d'
            }
        }        
    }
}
