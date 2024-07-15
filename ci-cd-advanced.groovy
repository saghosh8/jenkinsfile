pipeline {
    agent any

    environment {
        // Define environment variables for Docker image name, Artifactory credentials, and Kubernetes config
        DOCKER_IMAGE = "your-docker-image-name"
        ARTIFACTORY_URL = "https://your-artifactory-server/artifactory"
        ARTIFACTORY_REPO = "your-docker-repo"
        ARTIFACTORY_USER = credentials('artifactory-username') // Artifactory username from Jenkins credentials
        ARTIFACTORY_PASS = credentials('artifactory-password') // Artifactory password from Jenkins credentials
        KUBECONFIG = credentials('kubeconfig') // Kubernetes config file from Jenkins credentials
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

    stages {
        stage('Test') {
            steps {
                script {
                    echo 'Running tests...'
                    // Add your test commands here
                    // For example: sh 'npm test'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Building the project...'
                    // Add your build commands here
                    // For example: sh 'mvn clean package'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    echo 'Building Docker image...'
                    // Build the Docker image using the Dockerfile in the current directory
                    sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
                }
            }
        }

        stage('Docker Push to Artifactory') {
            steps {
                script {
                    echo 'Pushing Docker image to Artifactory...'
                    // Log in to Artifactory
                    sh """
                    docker login -u ${ARTIFACTORY_USER} -p ${ARTIFACTORY_PASS} ${ARTIFACTORY_URL}
                    // Tag the Docker image with the Artifactory URL and repository
                    docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${ARTIFACTORY_URL}/${ARTIFACTORY_REPO}/${DOCKER_IMAGE}:${BUILD_NUMBER}
                    // Push the Docker image to Artifactory
                    docker push ${ARTIFACTORY_URL}/${ARTIFACTORY_REPO}/${DOCKER_IMAGE}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Retrieve Docker Image from Artifactory') {
            steps {
                script {
                    echo 'Retrieving Docker image from Artifactory...'
                    // Log in to Artifactory
                    sh """
                    docker login -u ${ARTIFACTORY_USER} -p ${ARTIFACTORY_PASS} ${ARTIFACTORY_URL}
                    // Pull the Docker image from Artifactory
                    docker pull ${ARTIFACTORY_URL}/${ARTIFACTORY_REPO}/${DOCKER_IMAGE}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh 'kubectl apply -f deployment.yaml'
            }
        }
    }

    post {
        always {
            // Clean up the workspace after the pipeline finishes
            cleanWs()
        }
    }
}
