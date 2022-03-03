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
        /*stage ('Test') {
            steps {    
                sh ' mvn test'
            }
        }*/
         stage ('Imagem docker') {
            steps {
                sh 'docker build . -t vonex/integra_anomalia:${BUILD_NUMBER}'
            }
        }
        stage ('run app') {
            steps {
           		sh ' docker stop integra-anomalia' 
                sh ' docker rm integra-anomalia'	
                sh ' docker container run --network=host -h integra-anomalia -d --name integra-anomalia -v /usr/bin:/usr/bin -p 8096:8096 vonex/integra_anomalia:${BUILD_NUMBER}'
            }
        }
      
    }
}
