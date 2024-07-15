pipeline {
    agent any

    environment {
        ARTIFACTORY_URL = 'https://your-artifactory-url'
        ARTIFACTORY_REPO = 'your-repo-name'
        EMAIL_RECIPIENTS = 'devops@example.com,developer@example.com'
        PROJECT_NAME = 'your-project-name'
    }

    stages {
        stage('CI - Compile and Test') {
            steps {
                script {
                    // Compile code, run tests, and build the JAR
                    sh 'mvn clean package'
                }
            }
        }

        stage('Upload JAR to Artifactory') {
            steps {
                script {
                    // Upload the built JAR to Artifactory
                    sh "curl -u username:password -T target/${PROJECT_NAME}.jar ${ARTIFACTORY_URL}/${ARTIFACTORY_REPO}/${PROJECT_NAME}.jar"
                }
            }
        }

        stage('CD - Deploy to OpenShift') {
            steps {
                script {
                    // Pull the JAR from Artifactory and deploy using YAML
                    sh "curl -u username:password -O ${ARTIFACTORY_URL}/${ARTIFACTORY_REPO}/${PROJECT_NAME}.jar"
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
                        to: EMAIL_RECIPIENTS,
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
