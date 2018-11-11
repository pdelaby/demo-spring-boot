#!/usr/bin/env groovy


node {
    //stage('checkout') {
	//	git branch: 'master', url: 'https://github.com/pdelaby/demo-spring-boot.git'
    //}
	 
	stage('checkout'){
		checkout scm
	}

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
            sh "cp -R src/main/docker target/"
            sh "cp target/*.jar target/docker/"
            dockerImage = docker.build("fac/spring-boot-fac:latest", 'target/docker')
        
    }
		
	stage('start docker'){			
	    sh "docker stop spring-boot-fac"
	    sh "docker rm spring-boot-fac"
	    sh "docker run -d -name spring-boot-fac -p 8099:8080 fac/spring-boot-fac:latest"
	}
}