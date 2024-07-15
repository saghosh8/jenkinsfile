pipeline {
    agent any

    stages {
        stage('CI - Compile and Test') {
            steps {
                script {
                    // Compile code, run tests, and build the JAR
                    sh 'mvn clean package'
                }
            }
        }

        stage('Trigger JOB-1') {
            steps {
                build job: 'JOB-1', wait: true
            }
        }

        stage('Upload JAR to Artifactory') {
            steps {
                script {
                    // Upload the built JAR to Artifactory
                    sh "curl -u username:password -T target/your-project-name.jar https://your-artifactory-url/your-repo-name/your-project-name.jar"
                }
            }
        }

        stage('CD - Deploy to OpenShift') {
            steps {
                script {
                    // Pull the JAR from Artifactory and deploy using YAML
                    sh "curl -u username:password -O https://your-artifactory-url/your-repo-name/your-project-name.jar"
                    sh 'oc apply -f deployment.yaml'
                }
            }
        }

        stage('Send Email with Logs') {
            steps {
                script {
                    // Attach logs and send email
                    def logFile = 'build.log'
                    sh "cat ${logFile}" // Assuming logs are captured in build.log
                    emailext(
                        to: 'devops@example.com,developer@example.com',
                        subject: "Build Logs for ${env.BUILD_NUMBER}",
                        body: "Please find the attached logs for build #${env.BUILD_NUMBER}",
                        attachLog: true,
                        attachments: "${logFile}"
                    )
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }
    }
}
