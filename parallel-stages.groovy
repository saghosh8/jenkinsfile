/* Here is an example of a Jenkinsfile with parallel stages. This example runs the "Test" and "Build" stages in parallel, then proceeds to the "Deploy" stage.

 Jenkinsfile with Parallel Stages

```groovy */

pipeline {
    agent any

    stages {
        stage('Parallel Stages') {
            parallel {
                stage('Build') {
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
                    agent {
                        label 'test-agent'
                    }
                    steps {
                        echo 'Testing...'
                        // Run your tests here
                        sh 'mvn test'
                    }
                }
            }
        }
        stage('Deploy') {
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
```

 /* Explanation

1. Global Agent Block:
   - `agent any`: Specifies that any available agent can be used for this pipeline. This can be overridden at the stage level.

2. Parallel Stages Block:
   - `stage('Parallel Stages')`: Defines a stage that will contain parallel sub-stages.
   - `parallel { ... }`: Within this block, stages defined will run in parallel.

3. Build Stage:
   - `stage('Build')`: A sub-stage within the parallel block.
   - `agent { label 'build-agent' }`: Specifies the agent labeled 'build-agent' for the "Build" stage.
   - `steps { ... }`: Contains the steps to be executed in the "Build" stage, such as running a Maven build command.

4. Test Stage:
   - `stage('Test')`: Another sub-stage within the parallel block.
   - `agent { label 'test-agent' }`: Specifies the agent labeled 'test-agent' for the "Test" stage.
   - `steps { ... }`: Contains the steps to be executed in the "Test" stage, such as running tests.

5. Deploy Stage:
   - `stage('Deploy')`: Defines a stage named "Deploy".
   - `agent { label 'deploy-agent' }`: Specifies the agent labeled 'deploy-agent' for the "Deploy" stage.
   - `steps { ... }`: Contains the steps to be executed in the "Deploy" stage, such as using `kubectl` to apply a Kubernetes deployment configuration.

 Summary
In this Jenkinsfile:
- The "Build" and "Test" stages are defined within a parallel block and will run concurrently.
- Once both parallel stages are completed, the pipeline proceeds to the "Deploy" stage.
- Each stage can have its own specific agent, optimizing resource utilization and ensuring the appropriate environment is used for each task. */