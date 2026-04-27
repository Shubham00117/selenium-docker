pipeline {
    agent any

    environment {
        GRID_URL = 'http://localhost:5544'
    }

    stages {

        stage('Get Code') {
            steps {
                // download code from GitHub (main branch)
                git branch: 'main', url: 'https://github.com/Shubham00117/selenium-docker.git'
            }
        }

        stage('Start Grid') {
            steps {
                // start selenium grid using docker
                sh 'docker compose up -d'
            }
        }

        stage('Wait for Grid Ready') {
            steps {
                // wait until grid is ready
                sh '''
                    for i in {1..20}; do
                      if curl -s "$GRID_URL/status" | grep true; then
                        echo "Grid is ready"
                        exit 0
                      fi
                      echo "Waiting..."
                      sleep 2
                    done
                    exit 1
                '''
            }
        }

        stage('Run Tests') {
            steps {
                // run maven tests on grid
                sh '''
                    mvn test \
                    -Dexecution=grid \
                    -Dbrowser=chrome \
                    -Dgrid.url=$GRID_URL
                '''
            }
        }
    }

    post {
        always {
            // stop docker after execution
            sh 'docker compose down'
        }
    }
}