def call() {  
  pipeline {
    agent any
    stages{
    stage('Verify Workspace'){
      steps{
      script{
      sh """
      pwd
      Map pipelineConfig = readYaml(file: "Jenkinsfile.yaml")
      """
      }
    }
    }
    stage('Build'){
      steps{
        script{
      echo "Building: ${pipelineConfig.project_name}"
        }
      }
    }
  }
  }
}  
