// <!-- In a Jenkinsfile, you can use the when directive to conditionally execute stages based on certain criteria, 
// such as environment variables, parameters, or other conditions. 
// This is useful for controlling the flow of the pipeline based on specific conditions. -->

pipeline {
    agent any

    environment {
        BUILD_VERSION = '1.0.0'
        DEPLOY_ENV = 'production'
    }

    stages {
        stage('Build') {
            steps {
                echo "Building version ${BUILD_VERSION}..."
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                echo "Testing version ${BUILD_VERSION}..."
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            when {
                // Execute this stage only if DEPLOY_ENV is 'production'
                environment name: 'DEPLOY_ENV', value: 'production'
            }
            steps {
                echo "Deploying version ${BUILD_VERSION} to ${DEPLOY_ENV} environment..."
                sh 'kubectl apply -f deployment.yaml'
            }
        }
    }
}


// Summary
// The when directive allows conditional execution of stages based on the evaluation of specified criteria.
// This feature is useful for pipelines that need to handle multiple environments or scenarios, improving flexibility and efficiency.