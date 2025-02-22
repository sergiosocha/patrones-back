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
                    sh 'git clone https://github.com/sergiosocha/api-chart.git'
                }
            }
        }


        stage('Update Helm Chart') {
            steps {
                dir('api-chart') {
                   
                    sh 'chmod 666 values.yaml'
                    sh '''
                      docker run --rm -v "$(pwd)":/workdir mikefarah/yq eval '.image.repository = "sergios21/patrones-api"' -i values.yaml
                      docker run --rm -v "$(pwd)":/workdir mikefarah/yq eval '.image.tag = "latest"' -i values.yaml
                    '''
                }
            }
        }

        stage('Install Helm') {
            steps {
                sh '''
                  if ! command -v helm >/dev/null 2>&1; then
                    curl -LO https://get.helm.sh/helm-v3.11.2-linux-amd64.tar.gz
                    tar -zxvf helm-v3.11.2-linux-amd64.tar.gz
                    cp linux-amd64/helm ./helm
                    chmod +x ./helm
                  fi
                '''
            }
        }

        stage('Package Helm Chart') {
            steps {
                sh './helm package api-chart'
            }
        }

        stage('Push Helm Chart') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'HelmRepoCreds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    script {
                        sh '''
                          rm -rf helm-repo
                          git clone https://github.com/sergiosocha/api-chart-tgz.git helm-repo
                          cp "api-chart-0.1.0.tgz" helm-repo/charts/
                          cd helm-repo
                          git config user.name "CristianSz2003"
                          git config user.email "cristanchos2003@gmail.com"
                          git config credential.helper '!f() { echo "username=${USERNAME}"; echo "password=${PASSWORD}"; }; f'
                          git add .
                          git commit -m "Actualiza Chart"
                          git push
                        '''
                    }
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
