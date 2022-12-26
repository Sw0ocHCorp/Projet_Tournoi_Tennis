/*import { Dalle } from "dalle-node";

const dalle = new Dalle("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"); // Bearer Token 

(async () => {
  const generations = await dalle.generate("Portrait of a tennisman");

  console.log(generations)
})();*/

const { Configuration, OpenAIApi } = require("openai");
const fs = require('fs');
const commandLineArgs = require('command-line-args');

const optionDefinitions = [
    { name: 'prompt', alias: 'p', type: String, defaultValue: "an isometric view of a miniature city, tilt shift, bokeh, voxel, vray render, high detail" },
    { name: 'number', alias: 'n', type: Number, defaultValue: 1 },
    { name: 'size', alias: 's', type: Number, defaultValue: 256 },
];

const options = commandLineArgs(optionDefinitions);

const key = "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";

const configuration = new Configuration({
    apiKey: key
});
const openai = new OpenAIApi(configuration);


const predict = async function () {
    const response = await openai.createImage({
        prompt: "Portrait of a tenniswoman",
        n: 1,
        size: `256x256`,
        response_format: 'b64_json',
    });

    return response.data;
}


predict()
    .then(
        response => {
            for (let i = 0; i < response.data.length; i++) {
                const b64 = response.data[i]['b64_json'];
                const buffer = Buffer.from(b64, "base64");
                const filename = `C:\\Users\\nclsr\\OneDrive\\Bureau\\Cours_L3IA\\Base_de_Donnees_NoSQL\\Projet_Tournoi_Tennis\\src\\AiImageGenerated\\PortraitsJoueurs\\portrait_joueur${i}.png`;
                console.log("Writing image " + filename);
                fs.writeFileSync(filename, buffer);
            }
        }
    )


