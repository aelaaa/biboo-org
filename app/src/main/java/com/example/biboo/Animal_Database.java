package com.example.biboo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Animal_Database extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "animal_data";
    private static final String DB_name = "animalia";
    Cursor cursor;

    public Animal_Database(Context context) {
        super(context, DB_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                "(animal_id PRIMARY KEY AUTOINCREMENT, animal_name TEXT , scientific_name TEXT, classification TEXT, habitat TEXT, diet TEXT, description TEXT, trivia TEXT, image_src TEXT)";
        sqLiteDatabase.execSQL(query);

        // inserting animal data to database
        Animal_Data(sqLiteDatabase);
    }

    private void Animal_Data(SQLiteDatabase sqLiteDatabase) {
        // Animal data
        String[][] data = {
                // animal name, scientific name, classification, habitat, diet, description, trivia
                //Land Animals
                {"Cat", "Felis cat", "Land", "Forest", "Carnivore", "A small animal with fur, four legs, a tail, and claws, usually kept as a pet or for catching mice.", "Cats use their whiskers to measure gaps before deciding if they can fit through.", "drawable/animal_resources/img_cat.png"},
                {"Dog", "Cannis lupus familiaris", "Land", "Forest", "Carnivore", "A common animal with four legs, especially kept by people as a pet or to hunt or guard the family.", "Dogs have an extraordinary sense of smell, much better than humans.", "drawable/animal_resources/img_dog.png"},
                {"Pig", "Sus crofa domesticus", "Land", "Grassland", "Herbivore", "A large pink, brown, or black farm animal with short legs and a curved tail, kept for its meat.", "Pigs are highly intelligent animals; some even compare their smarts to that of dogs.", "drawable/animal_resources/img_pig.png"},
                {"Cow", "Bos taurus", "Land", "Grassland", "Herbivore", "A large female farm animal kept to produce meat and milk.", "Cows have four stomachs, helping them digest tough plant materials.", "drawable/animal_resources/img_cow.png"},
                {"Llama", "Lama glama", "Land", "Desert", "Herbivore", "A large animal with a long neck and long hair, often kept for its fur and to carry heavy loads.", "Llamas are excellent pack animals and can carry loads on mountainous terrains.", "drawable/animal_resources/img_llama.png"},
                {"Rabbit", "Oryctolagus coniculus", "Land", "Grassland", "Herbivore", "A small animal with long ears and large front teeth that moves by jumping on its long back legs.", "Rabbits have large ears to regulate their body temperature.", "drawable/animal_resources/img_rabbit.png"},
                {"Snake", "Serpentes", "Land", "Forest", "Carnivore", "A reptile with a long body, no legs, and produces hiss sounds.", "Snakes don't have eyelids, and their eyes are always open.", "drawable/animal_resources/img_snake.png"},
                {"Mouse", "Musm musculus", "Land", "Forest", "Herbivore", "A small mammal with short fur, a pointed face, a long tail, and often squeaks.","Mice can jump up to a foot in the air to avoid predators.", "drawable/animal_resources/img_mouse.png" },
                {"Zebra", "Equus quagga", "Land", "Grassland", "Herbivore", "An African wild animal that looks like a horse, with black or brown and white lines on its body.", "Every zebra has a unique pattern of black and white stripes.", "drawable/animal_resources/img_zebra.png"},
                {"Lion", "Panthera leo", "Land", "Grassland", "Carnivore", "A large wild animal of the cat family with yellowish-brown fur that is commonly known as the King of the Animal Kingdom.", "Lions are the only big cats that live in groups called prides.", "drawable/animal_resources/img_lion.png"},
                {"Water Buffalo", "Bubalus bubalis", "Land", "Grassland", "Herbivore", "A large animal of the cattle family, with long, curved horns and is often a companion of farmers in the field and pulling carts.", "Buffalo have a strong sense of community and often stay close to one another.", "drawable/animal_resources/img_buffalo.png"},
                {"Bear", "Ursidae", "Land", "Forest", "Omnivore", "A large, strong wild mammal with a thick fur coat that lives specially in colder parts of Europe, Asia, and North America.", "Bears hibernate during the winter months to conserve energy.", "drawable/animal_resources/img_bear.png"},
                {"Camel", "Camelus", "Land", "Desert", "A large animal with a long neck, that lives in the desert and has one or two humps on its back.", "Camels can drink a lot of water at once to store it in their bodies for later use.", "drawable/animal_resources/img_camel.png"},
                {"Reindeer", "Rangifer tarandus", "Land", "Forest", "Herbivore", "A type of deer that has horns like branches and lives in colder, northern parts of the world.","Both male and female reindeer grow antlers, unlike most other deer species.", "drawable/animal_resources/img_reindeer.png"},
                {"Giraffe", "Giraffa", "Land", "Grassland", "Herbivore", "A large animal with a spotted body, a very long neck, and long legs.", "Giraffes have long necks, but they only have seven neck vertebrae, the same as humans.", "drawable/animal_resources/img_giraffe.png"},
                {"Elephant", "Loxodonta", "Land", "Grassland", "Herbivore", "A very large grey mammal that has a trunk (long nose) with which it can pick things up.", "Elephants are the largest land animals, and they have excellent memories.", "drawable/animal_resources/img_elephant.png"},
                {"Weasel", "Mustela", "Land", "Forest", "Carnivore", "A small mammal with reddish-brown fur and a long body that feeds on other small animals such as mice and birds for food", "Weasels are agile hunters and can sneak into small spaces to catch prey.", "drawable/animal_resources/img_weasel.png"},
                {"Chimpanzee", "Pan troglodytes", "Land", "Forest", "Omnivore", "A small, very intelligent African ape with black or brown fur.", "Chimpanzees use tools, such as sticks, to extract insects from tree bark.", "drawable/animal_resources/img_chimpanzee.png"},
                {"Koala", "Phascolaractos cinereus", "Land", "Forest", "Herbivore", "An Australian mammal with greyish fur that lives in eucalyptus trees and eat their leaves.", "Koalas sleep for up to 20 hours a day due to their low-energy diet of eucalyptus leaves.", "drawable/animal_resources/img_koala.png"},
                {"Rhinoceros", "Rhinocerotidae", "Land", "Grassland", "Herbivore", "A very large, thick-skinned animal from Africa or Asia that has one or two horns on its nose.", "Rhinos have thick skin, but it is sensitive to sunburn and insect bites.", "drawable/animal_resources/img_rhinoceros.png"},
                {"Kangaroo", "Macropodidae", "Land", "Grassland", "Herbivore", "A large Australian mammal with a long stiff tail, short front legs and long powerful back legs on which it moves by jumping.", "Kangaroos use their powerful hind legs to hop at high speeds.", "drawable/animal_resources/img_kangaroo.png"},
                {"Raccoon", "Procyon lotor", "Land", "Forest", "Omnivore", "A small North American animal with black marks on its face and a long tail with black rings on it.", "Raccoons have nimble hands and are excellent at opening containers.", "drawable/animal_resources/img_racoon.png"},
                {"Anteater", "Myrmecophaga tridactyla", "Land", "Grassland", "Carnivore", "A mammal that eats ants or termites and has a long nose and tongue and no teeth.", "Anteaters have long tongues to slurp up ants and termites.", "drawable/animal_resources/img_anteater.png"},
                {"Hare", "Lepus", "Land", "Grassland", "Herbivore", "A small, furry animal with long ears that is like a large rabbit and can run fast.", "Hares are known for their fast sprints and high leaps to escape predators.", "drawable/animal_resources/img_hare.png"},
                {"Mole", "Talpidae", "Land", "Grassland", "Herbivore", "A small mammal that is almost blind, has dark fur, and lives in passages that it digs underground.", "Moles spend most of their lives underground and have velvety fur that allows them to move easily in tunnels.", "drawable/animal_resources/img_mole.png"},

                //Aquatic Animals
                {"Crab", "Brachyura", "Aquatic", "Ocean", "Omnivore", "A sea creature with five pairs of legs and a round, flat body covered by a shell, or its flesh eaten as food.", "Crabs can walk in any direction, not just forward and backward.", "drawable/animal_resources/img_crab.png"},
                {"Swordfish", "Xiphias gladius", "Aquatic", "Ocean", "Herbivore", "A large, long fish, with a very long pointed part like a beak at the front of its head.", "Swordfish use their long bills to slash through schools of fish.", "drawable/animal_resources/img_swordfish.png"},
                {"Shark", "Selachimorpha", "Aquatic", "Ocean", "Carnivore","A large fish that has sharp teeth and a pointed fin on its back.", "Sharks have skeletons made of cartilage, not bones.", "drawable/animal_resources/img_shark.png"},
                {"Jellyfish", "Scyphozoa", "Aquatic", "Ocean", "Carnivore", "A sea animal with a soft, round, almost transparent body with tentacles that can sting.", "Jellyfish have no brains or hearts.", "drawable/animal_resources/img_jellyfish.png"},
                {"Octopus", "Octopoda", "Aquatic", "Ocean", "Carnivore", "A sea creature with a soft, oval body and eight tentacles.", "Octopuses can change colors and textures to camouflage themselves.", "drawable/animal_resources/img_octopus.png"},
                {"Eel", "Anguilliformes", "Aquatic", "Lake", "Carnivore", "A long, thin, snake-like fish, some types of which are eaten.", "Eels can produce electric shocks to defend themselves or catch prey.", "drawable/animal_resources/img_eel.png"},
                {"Dolphin", "Cetacea", "Aquatic", "Ocean", "Carnivore", "A sea mammal that is large, smooth, and grey, with a long, pointed mouth.", "Dolphins are known for their playful behavior and high intelligence.", "drawable/animal_resources/img_dolphin.png"},
                {"Whale", "Cetacea", "Aquatic", "Ocean", "Omnivore", "A very large sea mammal that breathes air through a hole at the top of its head.", "Whales communicate through a series of songs that can travel vast distances underwater.", "drawable/animal_resources/img_whale.png"},
                {"Starfish", "Asteroidea", "Aquatic", "Ocean", "Omnivore", "A flat animal that lives in the sea and has five arms that grow from its body in the shape of a star.", "Starfish can regenerate lost arms.", "drawable/animal_resources/img_starfish.png"},
                {"Seal", "Pinnipedia", "Aquatic", "Arctic", "Carnivore", "A large mammal that eats fish and lives partly in the sea and partly on land or ice.", "Seals spend a lot of time sunbathing on rocks to regulate their body temperature.", "drawable/animal_resources/img_seal.png"},
                {"Tuna", "Thunnini", "Aquatic", "Ocean", "Carnivore", "An animal with elongated, robust, and rounded body that tapers to a slender tail base and a forked or crescent-shaped tail.", "Tunas are fast swimmers and can reach speeds of up to 75 km/h.", "drawable/animal_resources/img_tuna.png"},
                {"Sea Urchin", "Echinoidea", "Aquatic", "Ocean", "Herbivore", "A small sea creature that lives in water that is not very deep, has a round shell covered with sharp points like needles.", "Sea urchins have spines for protection and move using tube feet.", "drawable/animal_resources/img_sea_urchin.png"},
                {"Platypus", "Ornithorhynchus", "Aquatic", "River", "Carnivore", "An Australian mammal with a wide beak whose young are born from eggs.", "The platypus is a mammal that lays eggs instead of giving birth to live young.", "drawable/animal_resources/img_platypus.png"},
                {"Lobster", "Homarus americans", "Aquatic", "Ocean", "Carnivore", "An animal that lives in the sea and has a long body covered with a hard shell, two large claws, and eight legs.", "Lobsters have a strong sense of smell and taste with their antennae.", "drawable/animal_resources/img_lobster.png"},
                {"Flying Fish", " Exocoetidae", "Aquatic", "Ocean", "Herbivore", "A tropical fish that can jump above the surface of the water using its very large fins.", "Flying fish can glide through the air for distances up to 200 meters.", "drawable/animal_resources/img_flying_fish.png"},
                {"Sting Ray", "Myliobatoidei", "Aquatic", "Ocean", "Carnivore", "A large, flat, round fish with a long tail that has poisonous points on it.", "Stingrays are related to sharks and have a flattened body.", "drawable/animal_resources/img_sting_ray.png"},
                {"Sea Turtle", "Testudines", "Aquatic", "Ocean", "Omnivore", "A reptile that lives in the sea and has a thick shell covering its body into which it can move its head and legs for protection and come onto land in order to lay eggs.", "Sea turtles can hold their breath for several hours.", "drawable/animal_resources/img_sea_turtle.png"},
                {"Dugong", "Dugong dugon", "Aquatic", "Ocean", "Herbivore", "A large sea animal, similar to a manatee, that is found mainly in the Indian and Pacific oceans with thick skin, a divided fin at the back end of its body, and two flippers (flat parts for swimming) at the front.", "Dugongs are marine mammals that graze on seagrass.", "drawable/animal_resources/img_dugong.png"},
                {"Axolotl", "Ambystoma mexicanum", "Aquatic", "Lake", "Carnivore", "A small amphibian that lives in water and looks like a fish with four legs.", "Axolotls are unique because they remain in their aquatic larval form throughout their lives.", "drawable/animal_resources/img_axolotl.png"},
                {"Seahorse", "Hippocampus", "Aquatic", "Ocean", "Carnivore", "A type of fish whose head and neck look like those of a horse.", "Male seahorses carry and give birth to the babies.", "drawable/animal_resources/img_sea_horse.png"},
                {"Manta Ray", "Manta birostris", "Aquatic", "Ocean", "Carnivore", "A very large, flat sea fish with wing-shaped fins.", "Manta rays are filter feeders, using their gill rakers to strain plankton from the water.", "drawable/animal_resources/img_manta_rays.png"},
                {"Narwhal", "Monodon monoceros", "Aquatic", "Ocean", "Carnivore", "A medium-sized whale (large sea mammal) that has a long sharp tusk (tooth) growing out from its mouth.", "Narwhals have long, spiral tusks that can reach lengths of up to 10 feet.", "drawable/animal_resources/img_narwhal.png"},
                {"Sea Slug", "Nudibranchia", "Aquatic", "Lake", "Omnivore", "A small, usually black or brown creature with a long, soft body and no arms or legs, like a snail but with no shell.", "Sea slugs come in a variety of bright colors and interesting shapes.", "drawable/animal_resources/img_sea_slug.png"},
                {"Sea Anemone", "Actiniaria", "Aquatic", "Ocean", "Carnivore", "A soft, brightly coloured sea creature that looks like a flower and often lives on rocks under the water.", "Sea anemones are related to jellyfish and have stinging tentacles to catch prey.", "drawable/animal_resources/img_sea_anemone.png"},
                {"Walrus", "Odobenus rosmarus", "Aquatic", "Ocean", "Carnivore", "A mammal that lives in the sea and on beaches in the Arctic and is similar to a seal but larger, with two tusks and long hairs growing near its mouth.", "Walruses use their tusks to pull themselves out of the water and onto ice.", "drawable/animal_resources/img_walrus.png"},

                //Aerial Animals
                {"Parrot", "Psittaciformes", "Aerial", "Forest", "Herbivore", "A colorful and intelligent bird known for its ability to mimic human speech. ", "Parrots are known for their ability to mimic human speech.", "drawable/animal_resources/img_parrot.png"},
                {"Bat", "Chiroptera", "Aeiral", "Forest", "Omnivore", "A Nocturnal mammal capable of sustained flight using specialized wing membranes.", "Bats are the only mammals capable of sustained flight.", "drawable/animal_resources/img_bat.png"},
                {"Eagle", "Accipitrdae", "Aerial", "Grassland", "Carnivore", "A large bird of prey known for its keen eyesight and powerful flying abilities.", "Eagles have excellent eyesight and can spot prey from great distances.", "drawable/animal_resources/img_eagle.png"},
                {"Crow", "Corvus", "Aerial", "Grassland", "Carnivore", "A highly intelligent black bird known for adaptability and problem-solving skills.", "Crows are highly intelligent birds and can solve problems to obtain food.", "drawable/animal_resources/img_crow.png"},
                {"Peacocks", "Pavo cristatus", "Aerial", "Forest", "Carnivore", "A large and colorful bird, known for its extravagant and iridescent plumage. Peacocks are native to South Asia.", "Male peacocks use their colorful feathers to attract mates.", "drawable/animal_resources/img_peacocks.png"},
                {"Swan", "Cygnus", "Aerial", "Lake", "Herbivore", "Graceful waterfowl with long necks, often found in lakes and ponds.", "Swans mate for life and often stay with their partners year-round.", "drawable/animal_resources/img_swan.png"},
                {"Turkey", "Meleagris", "Aerial", "Grassland", "Omnivore", "Large domestic or wild bird, known for its distinctive fan-shaped tail.", "Turkeys can gobble to communicate with each other.", "drawable/animal_resources/img_turkey.png"},
                {"Owl", "Strigiformes", "Aerial", "Forest", "Carnivore", "Nocturnal bird of prey characterized by a flat face, forward-facing eyes, and silent flight.", "Owls are nocturnal birds of prey with the ability to rotate their heads up to 270 degrees.", "drawable/animal_resources/img_owl.png"},
                {"Hornbill", "Bucerotidae", "Aerial", "Forest", "Carnivore", "Large birds with distinctive, down-curved bills. They are often found in tropical and subtropical forests.", "Hornbills have unique casques on their bills, which vary in shape and size among species.", "drawable/animal_resources/img_hornbill.png"},
                {"Hawk", "Buteo", "Aerial", " Forest", "Carnivore", "Birds of prey with strong talons and excellent vision. They are skilled hunters and can be found in various habitats.", "Hawks are skilled hunters with keen eyesight.", "drawable/animal_resources/img_hawk.png"},
                {"Dove", "Columbidae", "Aerial", "Forest", "Herbivore", "Small to medium-sized birds known for their gentle cooing sounds. Often associated with peace.", "Doves are symbols of peace and are known for their gentle cooing sounds.", "drawable/animal_resources/img_dove.png"},
                {"Robin", "Turdus migratorius", "Aeiral", "Forest", "Herbivore", "Small songbirds with red or orange breasts. Common in gardens and woodlands.", "Robins are known for their red breasts and are a sign of spring in many cultures.", "drawable/animal_resources/img_robin.png"},
                {"Seagull", "Larinae", "Aerial", "Ocean", "Carnivore", "Medium to large seabirds commonly found near coastlines. They are opportunistic feeders.", "Seagulls are opportunistic feeders and can be found near coastlines scavenging for food.", "drawable/animal_resources/img_seagull.png"},
                {"Flamingo", "Phoenicopteridae", "Aerial", "Lake", "Carnivore", "Tall wading birds known for their pink plumage and distinctive curved bills. They are often found in shallow waters.", "Flamingos get their pink color from the food they eat, which contains pigments called carotenoids.", "drawable/animal_resources/img_flamingo.png"},
                {"Sparrow", "Passeridae", "Aerial", "Grassland", "Carnivore", "Small, social birds found in a variety of habitats. They are known for their chirping calls.", "Sparrows are social birds that often form large flocks.", "drawable/animal_resources/img_sparrow.png"},
                {"Raven", "Corvus corax", "Aerial", "Forest", "Carnivore", "Large black birds known for their intelligence and adaptability. Often associated with mythology and symbolism.", "Ravens are highly intelligent birds and are known for their problem-solving abilities.", "drawable/animal_resources/img_raven.png"},
                {"Falcon", "Falco", "Aerial", "Grassland", "Carnivore", "Birds of prey known for their speed and agility.", "Falcons are powerful birds of prey with strong beaks and talons.", "drawable/animal_resources/img_falcon.png"},
                {"Kookaburra", "Dacelo", "Aerial", "Forest", "Carnivore", "Large terrestrial kingfishers native to Australia, known for their distinctive laughing call.", "Kookaburras are known for their distinctive laughing call.", "drawable/animal_resources/img_kookaburra.png"},
                {"Woodpecker", "Picidae", "Aerial", "Forest", "Carnivore", "Birds with strong bills for drilling into wood to find insects. Often found in wooded areas.", "Woodpeckers use their strong bills to tap on trees to find insects.", "drawable/animal_resources/img_woodpecker.png"},
                {"Vulture", "Cathartes", "Aerial", "Grassland", "Carnivore", "Scavenging birds of prey that feed on carrion. Play a vital role in cleaning up ecosystems.", "Vultures play an important role in ecosystems by scavenging and cleaning up carcasses.", "drawable/animal_resources/img_vulture.png"},
                {"Toucan", "Ramphastos toco", "Aerial", "Forest", "Carnivore", "Colorful, large-billed birds found in tropical forests of Central and South America.", "Toucans have large, colorful bills that are light but strong.", "drawable/animal_resources/img_toucan.png"},
                {"Heron", "Ardeidae", "Aerial", "Lake", "Carnivore", "Wading birds with long legs and necks, often found in or near water.", "Herons are wading birds known for their long legs and graceful movements.", "drawable/animal_resources/img_heron.png"},
                {"Albatross", "Diomedeidae", "Aerial", "Ocean", "Carnivore", "Large seabirds known for their long wingspans. Skilled gliders and are found in the open ocean.", "Albatrosses have the longest wingspan of any bird, allowing them to soar for long distances.", "drawable/animal_resources/img_albatross.png"},
                {"Anhinga", "Anhinga anhinga", "Aeiral", "Lake", "Carnivore", "Water birds with a long neck and sharp bill, often found in freshwater habitats.", "Anhingas are expert divers and swim underwater to catch fish.", "drawable/animal_resources/img_anhinga.png"},
                {"Goose", "Anser cygnoides", "Aerial", "Lake", "Omnivore", "Waterfowl with a distinctive honking call, commonly found in various habitats, including lakes and grasslands.", "Geese fly in V-shaped formations during migration to conserve energy.", "drawable/animal_resources/img_goose.png"},

                //Prehistoric Animals
                {"Giant Beaver", "Castiroides", "Prehistoric", "Lake", "Herbivore", "An extinct species of beaver that lived during the Pleistocene epoch. enormous rodent that lived during the Ice Age.", "Castoroides, also known as the giant beaver, was an enormous rodent that lived during the Ice Age.", "drawable/animal_resources/img_giant_beaver.png"},
                {"Woolly Rhinoceros", "Coelodonta antiquitatis", "Prehistoric", "Grassland", "Herbivore", "An extinct species adapted to cold, Pleistocene environments, known for its long, thick fur and distinctive horn.", "The woolly rhinoceros, or Ceolodonta, had a thick coat of fur and a massive horn on its nose.", "drawable/animal_resources/img_woolly_rhinoceros.png"},
                {"Terminator Pig", "Entelodont", "Prehistoric", "Grassland", "Omnivore", "Commonly referred to as entelodonts or hell pigs, a family of prehistoric omnivores resembling a mix of pig and hyena. They had large heads, powerful jaws, and lived from the Eocene to Miocene epochs.", "Entelodon, often called the killer pig, was an omnivorous pig-like mammal with a robust build.", "drawable/animal_resources/img_terminator_pig.png"},
                {"Megapiranha", "Megapiranha paranensis", "Prehistoric", "River", "Carnivore", "An extinct species of large carnivore fish that lived during the Miocene epoch.", "Megapiranha is a bone-crushing predator taking bites of anything and everything", "drawable/animal_resources/img_megapiranha.png"},
                {"Megalodon", "Otodus megalodon", "Prehistoric", "Ocean", "Carnivore", "An extinct species of shark that lived approximately 23 to 3.6 million years ago. One of the largest predators to have ever existed.", "Megalodon was a prehistoric shark that ruled the oceans and is considered one of the largest predators to ever exist.", "drawable/animal_resources/img_megalodon.png"},
                {"Great Ground Slot", "Megatherium americanum", "Prehistoric", "Grassland" , "Herbivore", "Large, extinct sloths that lived during the Pleistocene epoch. They had robust builds and were adapted to terrestrial life.", "Megatherium, or the giant ground sloth, was a massive herbivorous mammal with large, curved claws.", "drawable/animal_resources/img_giant_ground_sloth.png"},
                {"Dodo Bird", "Raphus cucullatus", "Prehistoric", "Forest", "Omnivore", "An extinct flightless bird that lived on the island of Mauritius. It became extinct in the late 17th century due to human activities.", "The dodo bird, native to Mauritius, became extinct in the late 17th century, largely due to human activity.", "drawable/animal_resources/img_dodo_bird.png"},
                {"Titanoboa", "Titanoboa cerrejonensis", "Prehistoric", "Forest", " Carnivore", "An extinct species of giant snake that lived during the Paleocene epoch. It is considered one of the largest snakes ever discovered.", "Titanoboa was a gigantic, ancient snake that lived around 60 million years ago, dwarfing modern snakes.", "drawable/animal_resources/img_titanoboa.png"},
                {"Procoptodon", "Procoptodon goliah", "Prehistoric", "Desert", "Herbivore", "Extinct giant kangaroos that lived in Australia during the Pleistocene epoch. They were characterized by their large size and powerful hind legs.", "Procoptodon was a giant kangaroo with a unique feature. It could move by hopping on its hind legs.", "drawable/animal_resources/img_procoptodon.png"},
                {"Middle Lizard", "Mesosaurus", "Prehistoric", "Ocean", "Carnivore", "A genus of extinct aquatic animals that lived in the southern hemisphere during early Permian time.", "Mesosaurus was a prehistoric marine reptile that lived around 299 million years ago.", "drawable/animal_resources/img_middle_lizard.png"},
                {"Plesiosaurus", "Plesiosaurus dolichodeirus", "Prehistoric", "Ocean", "Carnivore", "An extinct marine reptile with a long neck and a body adapted for swimming. They lived during the Mesozoic era.", "Plesiosaurus was a marine reptile with a long neck and paddle-like limbs, resembling the mythical Loch Ness Monster.", "drawable/animal_resources/img_plesiosaurus.png"},
                {"Pliosaurus", "Pliosaurus funkei", "Prehistoric", "Ocean", "Carnivore", "A type of large predator marine reptile that lived during the Jurassic and Cretaceous periods. They are long-necked and short flippers, relative of Plesiosaurus", "Pliosaurus, a large marine predator, had a powerful jaw and sharp teeth, making it an apex predator of its time.", "drawable/animal_resources/img_pliosaurus.png"},
                {"Mammoth", "Mammuthus primigenius", "Prehistoric", "Arctic", "Herbivore", "Large, hairy elephants that lived during the Pleistocene epoch. They had long, curved tusks and were adapted to cold climates.", "The mammoth was a large, furry relative of the elephant that roamed during the Ice Age.", "drawable/animal_resources/img_mammoth.png"},
                {"Short-faced Bear", "Arctodus simus", "Prehistoric", "Forest", "Omnivore", "An extinct bear species with a short face and long limbs. It lived during the Pleistocene epoch.", "The short-faced bear, a Pleistocene giant, sported a distinctive short snout, dwarfing modern bears in size and strength while roaming North America.", "drawable/animal_resources/img_short-faced_bear.png"},
                {"Tully Monster", "Tullimonstrum gregarium", "Prehistoric", "Ocean", "Carnivore", "An extinct soft-bodied marine creature from the Pennsylvanian period. Its exact classification is still debated among scientists.", "The Tully Monster, a puzzling Pennsylvanian marine creature, mystifies scientists with its unusual body and long proboscis.", "drawable/animal_resources/img_tully_monster.png"},
                {"Gharial", "Gavialis gangeticus", "Prehistoric", "River", "Carnivore", "A species of crocodile native to the Indian subcontinent. They are characterized by their long, thin snouts.", "The gharial is a species of crocodile with a long, thin snout, primarily found in South Asian rivers.", "drawable/animal_resources/img_gharial.png"},
                {"Echidna", "Tachyglossus aculeatus", "Prehistoric", "Forest", "Carnivore", "A monotreme mammal native to Australia and New Guinea. They are known for their spines and are egg-laying mammals.", "Echidnas, also known as spiny anteaters, are egg-laying mammals found in Australia and New Guinea.", "drawable/animal_resources/img_echidna.png"},
                {"Tapir", "Tapirus terrestris", "Prehistoric", "Forest", "Herbivore", "Large herbivorous mammals with a distinctive trunk-like snout. Found in Central and South America, as well as Southeast Asia.", "Tapirs are large, herbivorous mammals with short trunks, resembling a mix between a pig and an elephant.", "drawable/animal_resources/img_tapir.png"},
                {"Andrewsarchus", "Andrewsarchus mongoliensis", "Prehistoric", "Desert", "Carnivore", "An extinct mammalian carnivore that lived during the Eocene epoch. It is considered one of the largest terrestrial mammals.", "Andrewsarchus is an extinct mammal that lived during the Eocene epoch and is considered one of the largest terrestrial mammals.", "drawable/animal_resources/img_andrewsarchus.png"},
                {"Balanerpeton", "Balanerpeton woodi", "Prehistoric", "River", "Carnivore", "Aquatic creature with a long body and limbs, resembling a primitive amphibian, known for its carnivorous diet.", "This ancient amphibian, Balanerpeton, thrived around 300 million years ago and showcased early adaptations for life in the water.", "drawable/animal_resources/img_balanerpeton.png"},
                {"Arthropleura", "Arthropleura armata", "Prehistoric", "Forest", "Herbivore", "Massive terrestrial arthropod, resembling a giant millipede, characterized by numerous body segments and legs, primarily herbivorous.", "Arthropleura, a giant millipede, ruled terrestrial ecosystems around 300 million years ago, growing up to lengths of 2.6 meters.", "drawable/animal_resources/img_arthropleura.png"},
                {"Dimetrodon", "Dimetrodon limbatus", "Prehistoric", "Coastal Region", "Carnivore", "Prehistoric reptile with a sail-like structure on its back, lived in coastal regions, and was a carnivorous predator.", "Dimetrodon, with its sail-like structure, wasn't a dinosaur but rather a prehistoric relative. It lived around 295 million years ago and was an apex predator.", "drawable/animal_resources/img_dimetrodon.png"},
                {"Glyptodon", "Glyptodon clavipes", "Prehistoric", "Grassland", "Herbivore", "Large, heavily armored mammal-like creature, related to armadillos, adapted to terrestrial life, and primarily herbivorous.", "Glyptodon, a massive armored mammal, roamed South America during the Pleistocene epoch, resembling a super-sized armadillo.", "drawable/animal_resources/img_glyptodon.png"},
                {"Bothriolepis", "Bothriolepis canadensis", "Prehistoric", "Ocean", "Omnivore", "Aquatic fish with distinctive armor plates, likely exhibited omnivorous behavior in ocean environments.", "Bothriolepis, an ancient fish, sported distinctive armor plates, flourishing in Devonian oceans over 360 million years ago.", "drawable/animal_resources/img_bothriolepis.png"},
                {"Diplocaulus", "Diplocaulus magnicornis", "Prehistoric", "Lake", "Carnivore", "Amphibian with a unique boomerang-shaped head, lived in aquatic environments, and primarily carnivorous.", "The Diplocaulus, with its boomerang-shaped head, navigated prehistoric waters around 270 million years ago, showcasing unique adaptations among amphibians.", "drawable/animal_resources/img_diplocaulus.png"}
        };

        ContentValues values;

        for (String[] row : data) {
            values = new ContentValues();
            values.put("animal_name", row[1]);
            values.put("scientific_name", row[2]);
            values.put("classification", row[3]);
            values.put("habitat", row[4]);
            values.put("diet", row[5]);
            values.put("description", row[6]);
            values.put("trivia", row[7]);
            values.put("image_src", row[8]);

            sqLiteDatabase.insert(TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor getAnimal(int animalId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE animal_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(animalId)});
    }


    // this is the code you will be using if you are going to access the data
    // Note this would be coded to other java where you need to access getAnimal method
    /*
        AnimalDatabase animal_data = new AnimalDatabase(context);
            int animalID = // this will be used for the result of random number generator
            Cursor cursor = animal_data.getAnimal(animalId);

            if (cursor != null && cursor.moveToFirst()) {
            String animal_name = cursor.getString(cursor.getColumnIndex("animal_name"));
            String scientific_name = cursor.getString(cursor.getColumnIndex("scientific_name"));
            String classification = cursor.getString(cursor.getColumnIndex("classification"));
            String habitat = cursor.getString(cursor.getColumnIndex("habitat"));
            String diet = cursor.getString(cursor.getColumnIndex("diet"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String trivia = cursor.getString(cursor.getColumnIndex("trivia"));
            String img_src = cursor.getString(cursor.getColumnIndex("image_src"));
            }
                // Don't forget to close the cursor when done with it
                if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                  }
     */
}