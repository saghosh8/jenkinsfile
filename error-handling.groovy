pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                script {
                    try {
                        sh 'mvn clean install'
                    } catch (Exception e) {
                        echo "Build failed: ${e.message}"
                        currentBuild.result = 'FAILURE'
                    } finally {
                        // Cleanup actions, e.g., archiving logs
                    }
                }
            }
        }
    }
}
