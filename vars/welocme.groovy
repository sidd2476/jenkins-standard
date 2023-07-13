def call() { 
  Map pipelineConfig = [:]
  pipeline {
    agent any
    stages{
    stage('Verify Workspace'){
      steps{
      script{      
      pipelineConfig = readYaml(file: "Jenkinsfile.yaml")
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
