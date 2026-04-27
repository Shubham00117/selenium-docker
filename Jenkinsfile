pipeline {
    agent any

    environment {
        GRID_URL = 'http://localhost:5544'
    }

    stages {

        stage('Check Grid') {
            steps {
                sh '''
                    echo "Checking Selenium Grid..."

                    curl "$GRID_URL/status" || exit 1
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