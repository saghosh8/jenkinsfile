/* To add an agent to each stage in a Jenkinsfile, you specify the agent block within each stage. 
This allows you to define different agents for different stages of your pipeline, 
providing flexibility in your CI/CD process. Here's how you can do it: */

pipeline {
    // Define a global agent that will be used if no agent is specified in a stage
    agent none

    stages {
        stage('Build') {
            // Define the agent for the Build stage
            agent {
                label 'build-agent'
            }
            steps {
                echo 'Building...'
                // Use the appropriate build tool here, e.g., Maven, Gradle, npm, etc.
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            // Define the agent for the Test stage
            agent {
                label 'test-agent'
            }
            steps {
                echo 'Testing...'
                // Run your tests here
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            // Define the agent for the Deploy stage
            agent {
                label 'deploy-agent'
            }
            steps {
                echo 'Deploying...'
                // Deploy the application, e.g., using Kubernetes, Docker, etc.
                sh 'kubectl apply -f deployment.yaml'
            }
        }
    }
}


/* Explanation
Global Agent Block:

agent none: This specifies that no global agent will be used. Instead, agents will be defined per stage.
Stage-Specific Agent Block:

stage('Build'): Defines a stage named "Build".
agent { label 'build-agent' }: Specifies that the "Build" stage should run on an agent labeled 'build-agent'.
steps { ... }: Contains the steps to be executed in the "Build" stage, such as running a Maven build command.
Test Stage:

Similar to the "Build" stage, but with agent { label 'test-agent' } to run on an agent labeled 'test-agent'.
Includes steps to run tests.
Deploy Stage:

Uses agent { label 'deploy-agent' } to run on an agent labeled 'deploy-agent'.
Contains steps to deploy the application, such as using kubectl to apply a Kubernetes deployment configuration.
By defining agents at the stage level, you can optimize resource utilization and ensure that each stage runs on the most appropriate environment for its tasks. 
This is especially useful in environments with heterogeneous build agents, where different stages might require different tools or configurations. */