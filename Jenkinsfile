pipeline {
    agent any

    environment {
        GRID_URL = 'http://localhost:5544'
    }

    stages {

        stage('Start Selenium Grid') {
            steps {
                // start docker grid
                sh 'docker compose up -d'
            }
        }

        stage('Wait for Grid Ready') {
            steps {
                // wait until selenium grid is ready
                sh '''
                    for i in $(seq 1 20); do
                      if curl -s "$GRID_URL/status" | grep -q '"ready":true'; then
                        echo "Grid is ready"
                        exit 0
                      fi
                      echo "Waiting for Grid..."
                      sleep 2
                    done

                    echo "Grid not ready"
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