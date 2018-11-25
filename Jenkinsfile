#!/usr/bin/env groovy


// Ne garder que 5 builds et 5 artefacts
properties([buildDiscarder(logRotator( artifactNumToKeepStr: '5', numToKeepStr: '5'))])


node {

	def imageName = "spring-boot-fac"
    stage('checkout') {
		git branch: 'master', url: 'https://github.com/pdelaby/demo-spring-boot.git'
    }
	 
	//stage('checkout'){
	//	checkout scm
	//}

	// si jenkins est dans un docker, il faut rajouter -u root
    docker.image('openjdk:8').inside('-e MAVEN_OPTS="-Duser.home=./"') {
		stage('check tools') {
             parallel(
                 java: {
                    sh "java -version"
                },
                maven: {
                    sh "chmod +x mvnw"
                    sh "./mvnw -version"
                }
            )
        }
        
        
		stage('clean') {
			sh "./mvnw clean"
		}
        
		stage('backend tests') {
            try {
                sh "./mvnw test"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
 
        stage('packaging') {
            sh "./mvnw verify -Pprod -DskipTests"
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }
	
	def dockerImage
    stage('build docker') {
            sh "git clone https://github.com/pdelaby/docker-for-spring-boot.git target/docker"
            sh "cp target/*.jar target/docker/"
            dockerImage = docker.build("fac/${imageName}:latest", 'target/docker')       
    }

	
	stage('stop et rm docker'){	
		def imageWC = sh(script: "docker ps -a -q --filter \"name=${imageName}\" | wc -l", returnStdout: true).trim()

		if ( imageWC == "1" ){
			sh "docker stop ${imageName}"
			sh "docker rm ${imageName}"
		}else{
			println 'Image non demarree'
		}
	    
	}
	stage('start docker'){			
	    sh "docker run -d --name ${imageName} -p 8051:8080 fac/${imageName}:latest"
	}
}