// To include manual input in a Jenkins pipeline, you can use the input step. 
// This allows the pipeline to pause and wait for a user to provide input or approve a step before proceeding. 
// Here’s an example Jenkinsfile that incorporates a manual input step:

// Jenkinsfile with Manual Input

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // This step will print 'Building...' to the console output
                echo 'Building...'
                // Add your build commands here, e.g., compiling code, running tests, etc.
            }
        }
        stage('Manual Approval') {
            steps {
                script {
                    // This step pauses the pipeline and waits for user input
                    // The message displayed to the user is 'Approve deployment?'
                    // The button the user needs to click to continue is labeled 'Deploy'
                    input message: 'Approve deployment?', ok: 'Deploy'
                }
            }
        }
        stage('Deploy') {
            steps {
                // This step will print 'Deploying...' to the console output
                echo 'Deploying...'
                // Add your deployment commands here, e.g., deploying to a server, updating a database, etc.
            }
        }
    }
}
