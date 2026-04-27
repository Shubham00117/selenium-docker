pipeline {
    agent any

    environment {
        GRID_URL = 'http://localhost:5544'
    }

    stages {

        stage('Start Selenium Grid') {
            steps {
                sh '''
                    echo "Starting Selenium Grid..."
                    docker compose down || true
                    docker compose up -d
                '''
            }
        }

        stage('Wait for Grid Ready') {
            steps {
                sh '''
                    echo "Waiting for Selenium Grid..."

                    for i in $(seq 1 60); do
                      STATUS=$(curl -s "$GRID_URL/status")

                      echo "Attempt $i"
                      echo "$STATUS"

                      if echo "$STATUS" | grep -q '"ready":true'; then
                        echo "Grid is READY ✅"
                        exit 0
                      fi

                      sleep 3
                    done

                    echo "Grid NOT READY ❌"
                    echo "===== DOCKER LOGS ====="
                    docker compose logs --tail=200

                    exit 1
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                    echo "Running Tests..."

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
            sh '''
                echo "Stopping Docker..."
                docker compose down
            '''
        }
    }
}