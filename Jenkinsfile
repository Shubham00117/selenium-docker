pipeline {
    agent any

    environment {
        GRID_URL = 'http://localhost:4444'
    }

    stages {

        stage('Check Grid') {
            steps {
                sh '''
                    echo "Checking Selenium Grid..."

                    RESPONSE=$(curl -s --max-time 5 "$GRID_URL/status")

                    if [ -z "$RESPONSE" ]; then
                      echo "Grid not reachable ❌"
                      exit 1
                    fi

                    echo "$RESPONSE"

                    if echo "$RESPONSE" | grep -Eq '"ready"[[:space:]]*:[[:space:]]*true'; then
                      echo "Grid is READY ✅"
                    else
                      echo "Grid not ready ❌"
                      exit 1
                    fi
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
}