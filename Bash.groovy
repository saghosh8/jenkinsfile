// To implement a timeout in a Jenkins pipeline using a Bash script, 
// you can use the timeout command in your shell command. 
// Here’s an example Jenkinsfile that demonstrates this:


pipeline {
    agent any

//Defines environment variables `BUILD_VERSION` as '1.0.0' and `DEPLOY_ENV` as 'production' for use in the pipeline. 
    environment {
        BUILD_VERSION = '1.0.0'
        DEPLOY_ENV = 'production'
    }

    //Defines a 'Build' stage that echoes the build version and runs `mvn clean package` to clean and package the project.
    stages {
        stage('Build') {
            steps {
                echo "Building version ${BUILD_VERSION}..."
                sh 'mvn clean package'
            }
        }
        
    //Defines a 'Test' stage that echoes the build version and runs `mvn test` to execute the project's tests.
        stage('Test') {
            steps {
                echo "Testing version ${BUILD_VERSION}..."
                sh 'mvn test'
            }
        }

    //Defines a 'Deploy' stage that echoes the build version and deployment environment, 
    //then runs `kubectl apply -f deployment.yaml` with a timeout of 60 seconds to deploy the application.
        stage('Deploy') {
            steps {
                echo "Deploying version ${BUILD_VERSION} to ${DEPLOY_ENV} environment..."
                // Run a deployment command with a timeout of 60 seconds
                sh 'timeout 60s kubectl apply -f deployment.yaml'
            }
        }
    }
}
