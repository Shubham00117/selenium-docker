pipeline {
    agent any

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Select browser to run tests'
        )
    }

    environment {
        GRID_URL = 'http://localhost:4444'
    }

    stages {

        // ✅ Step 1: Start Docker Grid
        stage('Start Selenium Grid') {
            steps {
                sh '''
                    echo "Starting Selenium Grid using Docker..."
                    docker compose down || true
                    docker compose up -d

                    echo "Waiting for Grid to be ready..."
                    sleep 10
                '''
            }
        }

        // ✅ Step 2: Check Grid
        stage('Check Grid') {
            steps {
                sh '''
                    echo "Checking Selenium Grid..."

                    for i in {1..10}
                    do
                      RESPONSE=$(curl -s --max-time 5 "$GRID_URL/status")

                      if echo "$RESPONSE" | grep -Eq '"ready"[[:space:]]*:[[:space:]]*true'; then
                        echo "Grid is READY ✅"
                        exit 0
                      fi

                      echo "Waiting for Grid... ⏳"
                      sleep 5
                    done

                    echo "Grid not ready ❌"
                    exit 1
                '''
            }
        }

        // ✅ Step 3: Run Tests
        stage('Run Tests') {
            steps {
                sh """
                    echo "Running Tests on ${params.BROWSER}..."

                    mvn clean test \
                    -Dexecution=grid \
                    -Dbrowser=${params.BROWSER} \
                    -Dgrid.url=$GRID_URL
                """
            }
        }
    }

    // ✅ Step 4: Reports + Cleanup
    post {
        always {
            echo "Publishing Test Reports..."

            // ✅ Correct JUnit path (TestNG XML reports)
            junit allowEmptyResults: true, testResults: 'test-output/junitreports/*.xml'

            // ✅ Archive all reports (Extent + logs)
            archiveArtifacts artifacts: 'test-output/**/*.*', allowEmptyArchive: true

            // ✅ Extent HTML Report (SAFE - won’t fail if plugin missing)
            script {
                try {
                    publishHTML([
                        reportDir: 'test-output/extent-reports',
                        reportFiles: '*.html',
                        reportName: 'Extent Report',
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true
                    ])
                } catch (Exception e) {
                    echo "HTML Publisher Plugin not installed, skipping HTML report..."
                }
            }

            // ✅ Stop Docker after execution
            sh '''
                echo "Stopping Docker Grid..."
                docker compose down
            '''
        }
    }
}