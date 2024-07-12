/* Jenkinsfile with Environment Variables and Explanations
 */

pipeline {
    // Specify the agent where the pipeline will run
    agent any
    
    // Define environment variables available throughout the pipeline
    environment {
        BUILD_VERSION = '1.0.0'
        DEPLOY_ENV = 'production'
    }
    
    stages {
        stage('Build') {
            steps {
                // Use the BUILD_VERSION environment variable in a message
                echo "Building version ${BUILD_VERSION}..."
                // Example build command using Maven
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                // Use the BUILD_VERSION environment variable in a message
                echo "Testing version ${BUILD_VERSION}..."
                // Example test command using Maven
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                // Use the BUILD_VERSION and DEPLOY_ENV environment variables in a message
                echo "Deploying version ${BUILD_VERSION} to ${DEPLOY_ENV} environment..."
                // Example deploy command using kubectl
                sh 'kubectl apply -f deployment.yaml'
            }
        }
    }
}


/* The environment variables BUILD_VERSION and DEPLOY_ENV are defined globally and used across different stages.
These variables provide consistent values that can be easily changed in one place, ensuring all stages use the same configuration.
 */