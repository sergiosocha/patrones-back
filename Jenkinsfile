pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'github-token'
    }
    
    stages {
        stage('Checkout Application') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-token',
                    url: 'https://github.com/sergiosocha/patrones-back.git'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t sergios21/patrones-api .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'Dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh "docker login -u $DOCKER_USER -p $DOCKER_PASSWORD"
                    sh 'docker push sergios21/patrones-api'
                }
            }
        }

        stage('Checkout Helm Chart') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'HelmRepoCreds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'rm -rf api-chart'
                    sh 'git clone https://${USERNAME}:${PASSWORD}@github.com/sergiosocha/api-chart.git'
                }
            }
        }


        stage('Update Helm Chart') {
            steps {
                
                sh '''
                docker run --rm -v "$(pwd)":/workdir mikefarah/yq eval '.image.repository = "sergios21/patrones-api"' -i values.yaml
                '''
                
                sh '''
                docker run --rm -v "$(pwd)":/workdir mikefarah/yq eval '.image.tag = "latest"' -i values.yaml
                '''
            }
        }

        stage('Package Helm Chart') {
            steps {
                sh 'helm package .'
            }
        }

        stage('Push Helm Chart') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'HelmRepoCreds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh '''
                      curl -u ${USERNAME}:${PASSWORD} \
                           --upload-file *.tgz \
                           https://github.com/sergiosocha/api-chart-tgz
                    '''
                }
            }
        }

        stage('Update ArgoCD Manifest') {
            steps {
                git branch: 'main',
                    credentialsId: 'GitHubCreds',
                    url: 'https://github.com/sergiosocha/api-chart.git'
                
                sh '''
                  yq eval '.spec.template.spec.containers[0].image = "sergios21/patrones-api:latest"' -i deployment.yaml
                  git add deployment.yaml
                  git commit -m "Update Argo manifest with latest image"
                  git push origin main
                '''
            }
        }
    }
}
