pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from Git
                git branch: 'main', url: 'https://github.com/Arrtty/apirest-products'
            }
        }

        stage('Build') {
            steps {
                script {
                    // Run the Maven build to compile the code and package the application
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Run the tests using Maven
                    sh './mvnw test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image for the application
                    sh 'docker build -t arty2/apirest-products:latest .'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Run the Docker container
                    sh 'docker run -d -p 9000:9000 arty2/apirest-products:latest'
                }
            }
        }
    }

    post {
        always {
            // Clean up the workspace
            cleanWs()
        }
        success {
            // Notify success
            echo 'Pipeline completed successfully!'
        }
        failure {
            // Notify failure
            echo 'Pipeline failed.'
        }
    }
}
