// Below is a Jenkinsfile that demonstrates how to pass parameters and set up triggers. This example will include:

// Parameters for the pipeline.
// A build trigger based on a schedule.

pipeline {
    agent any
    
    // Define parameters
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
        booleanParam(name: 'DEPLOY_TO_PRODUCTION', defaultValue: false, description: 'Deploy to production?')
    }

    // Define triggers
    triggers {
        // Cron trigger to schedule the job every day at midnight
        cron('H H * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the specified branch
                checkout([$class: 'GitSCM', branches: [[name: "${params.BRANCH_NAME}"]], 
                          userRemoteConfigs: [[url: 'https://github.com/your-repo/your-project.git']]])
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                // Your build commands here
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                // Your test commands here
            }
        }
        stage('Deploy') {
            when {
                expression { return params.DEPLOY_TO_PRODUCTION }
            }
            steps {
                echo 'Deploying to production...'
                // Your deployment commands here
            }
        }
    }
}
