pipeline {
    agent any

    environment {
        GIT_BRANCH = "${env.BRANCH_NAME}"
        DOCKER_IMAGE_NAME = "my-app:${GIT_BRANCH}"
        KUBERNETES_NAMESPACE = "my-namespace"
        SONARQUBE_HOST = "sonarqube-server"
        SONARQUBE_TOKEN = "your-token"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/your-repo.git', branch: "${GIT_BRANCH}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube Server') {
                    sh 'sonar-scanner'
                }
            }
        }

        stage('Code Quality') {
            steps {
                sh 'pmd:checkstyle'
                sh 'pmd:cpd'
            }
        }

        stage('Docker Build') {
            steps {
                docker.build(DOCKER_IMAGE_NAME)
            }
        }

        stage('Docker Push') {
            steps {
                docker.withRegistry('https://registry.docker.io', 'dockerhub_creds') {
                    docker.image(DOCKER_IMAGE_NAME).push()
                }
            }
        }

        stage('Kubernetes Deployment') {
            steps {
                script {
                    def kubectl = new KubernetesClient(kubeconfigFile: '/path/to/kubeconfig')
                    kubectl.load('deployment.yaml')
                    kubectl.deploy(namespace: KUBERNETES_NAMESPACE)
                }
            }
        }
    }

    post {
        always {
            script {
                cleanupWorkspace()
            }
        }
        success {
            script {
                mail to: 'your_email@example.com', subject: 'Build Successful', body: "Build ${env.JOB_NAME} #${env.BUILD_NUMBER} has succeeded."
            }
        }
        failure {
            script {
                mail to: 'your_email@example.com', subject: 'Build Failed', body: "Build ${env.JOB_NAME} #${env.BUILD_NUMBER} has failed."
            }
        }
    }
}
