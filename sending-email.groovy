// To send emails in a Jenkins pipeline based on the success, failure, or always condition, 
// you can use the post section of the pipeline. 
// Here’s an example Jenkinsfile demonstrating how to send emails in these scenarios:

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
            steps {
                echo "Deploying version ${BUILD_VERSION} to ${DEPLOY_ENV} environment..."
                sh 'timeout 60s kubectl apply -f deployment.yaml'
            }
        }
    }

    post {
        success {
            // Send email on successful completion
            mail to: 'recipient@example.com',
                 subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build was successful!\n\nCheck console output at ${env.BUILD_URL}"
        }
        failure {
            // Send email on failure
            mail to: 'recipient@example.com',
                 subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build has failed.\n\nCheck console output at ${env.BUILD_URL}"
        }
        always {
            // Send email regardless of build status
            mail to: 'recipient@example.com',
                 subject: "Build Completed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "The build has completed.\n\nCheck console output at ${env.BUILD_URL}"
        }
    }
}
