// A comprehensive Jenkinsfile that incorporates a complete CI/CD pipeline, 
// including all specified steps and some additional best practices:

pipeline {
    agent any

    environment {
        GIT_REPO = 'https://your-repo-url.git'
        IMAGE_NAME = 'your-image-name'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        ARTIFACTORY_URL = 'https://your-artifactory-url'
        K8S_DEPLOYMENT_NAME = 'your-k8s-deployment'
        NAMESPACE = 'your-namespace'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        // Other stages
    }
        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                // Run JUnit or Selenium tests using Maven
                sh 'mvn test'
            }
        }
        stage('Build JAR') {
            steps {
                echo 'Building JAR file...'
                // Package the application using Maven
                sh 'mvn clean package'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                // Build Docker image
                sh "docker build -t ${ARTIFACTORY_URL}/${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }
        stage('Push Docker Image to Artifactory') {
            steps {
                echo 'Pushing Docker image to Artifactory...'
                // Log in to Artifactory and push the image
                sh "docker login ${ARTIFACTORY_URL} -u your-username -p your-password"
                sh "docker push ${ARTIFACTORY_URL}/${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }
        stage('Deploy to OpenShift') {
            steps {
                echo 'Deploying Docker image to OpenShift...'
                // Use kubectl or oc to deploy the image
                sh "oc rollout restart deployment/${K8S_DEPLOYMENT_NAME} -n ${NAMESPACE}"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
            // Send email or notification if needed
        }
        failure {
            echo 'Pipeline failed.'
            // Send email or notification if needed
        }
        always {
            echo 'Cleaning up...'
            // Any cleanup steps can be added here
        }
    }
}


