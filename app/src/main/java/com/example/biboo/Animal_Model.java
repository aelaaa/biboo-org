package com.example.biboo;

public class Animal_Model {
    String animal_name, scientificName, classification, habitat, diet, description, trivia, img_source;
    int id;

    public Animal_Model(int id, String animal, String scientific_name, String classification, String habitat, String diet, String description, String trivia, String image_source) {
        this.id = id;
        this.animal_name = animal;
        this.scientificName = scientific_name;
        this.classification = classification;
        this.habitat = habitat;
        this.diet = diet;
        this.trivia = trivia;
        this.description = description;
        this.img_source = image_source;
    }
        //get and set method for setting and getting data retrieval
        public int getId() {return id;}
        public void setId() {this.id = id;}
        public String getAnimal_Name() {
            return animal_name;
        }
        public void setAnimal_Name(String animal) {this.animal_name = animal;}
        public String getScientificName() {return scientificName;}
        public void setScientificName(String scientificName) {this.scientificName = scientificName;}
        public String getClassification() {return classification;}
        public void setClassification(String classification) {this.classification = classification;}
        public String getHabitat(){return habitat;}
        public void setHabitat(String habitat) {this.habitat = habitat;}
        public String getDiet() {return diet;}
        public void setDiet(String diet) {this.diet = diet;}
        public String getDescription() {return description;}
        public void setDescription(String description) {this.description = description;}
        public String getTrivia() {return trivia;}
        public void setTrivia() {this.trivia = trivia;}
        public String getImageSource() {
            return img_source;
        }

        public void setImageSource(String image_source) {
            this.img_source = image_source;
        }

}
