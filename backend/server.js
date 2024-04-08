import express from 'express';
import fs from 'fs';
import { fileURLToPath } from 'url';
import path from 'path';
import cors from 'cors';
import bodyParser from 'body-parser';

const app = express();
const port = 3000;

app.use(cors()); // Enable Cross-Origin Resource Sharing
app.use(express.json()); // Parse incoming JSON requests
// var urlencodedParser = bodyParser.urlencoded({ extended: true }); // Parse incoming URL-encoded form data

// Middleware to parse JSON requests
app.use(express.json());

// Getting the directory name using fileURLToPath and dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const filePath = path.join(__dirname, 'data.json');

app.use(express.json());

app.get('/', (req, res) => {
   res.status(200).json({message: "Root"})
});
// Route to get data
app.get('/data', (req, res) => {
  try {
    const data = fs.readFileSync(filePath, 'utf8');
    res.json(JSON.parse(data));
  } catch (error) {
    console.error('Error reading data from file:', error);
    res.status(500).json({ error: 'Failed to read data' });
  }
});

app.delete('/deleteReward/:email/:index', (req, res) => {
  const email = req.params.email;
  const index = parseInt(req.params.index);

  // Read the JSON file
  fs.readFile('data.json', 'utf8', (err, data) => {
      if (err) {
          console.error(err);
          res.status(500).send('Error reading file');
          return;
      }

      let jsonData = JSON.parse(data);

      // Find the user
      const userIndex = jsonData.findIndex(item => item.email === email);
      if (userIndex === -1) {
          res.status(404).send('User not found');
          return;
      }

      // Check if index is valid
      if (index < 0 || index >= jsonData[userIndex].rewards.length) {
          res.status(400).send('Invalid index');
          return;
      }

      // Remove the entry
      jsonData[userIndex].rewards.splice(index, 1);

      // Write the updated data back to file
      fs.writeFile('data.json', JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Reward entry deleted successfully');
      });
  });
});

app.put('/updateIncome/:email', (req, res) => {

  console.log(req.body);

  const email = req.params.email;
  const newIncome = req.body.income;

  // Read the JSON file
  fs.readFile('data.json', 'utf8', (err, data) => {
      if (err) {
          console.error(err);
          res.status(500).send('Error reading file');
          return;
      }

      let jsonData = JSON.parse(data);

      // Find the user
      const userIndex = jsonData.findIndex(item => item.email === email);
      if (userIndex === -1) {
          res.status(404).send('User not found');
          return;
      }

      // Update the income
      jsonData[userIndex].income = newIncome;

      // Write the updated data back to file
      fs.writeFile('data.json', JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Income updated successfully');
      });
  });
});

app.put('/updateBudget/:email', (req, res) => {
  
  const email = req.params.email;
  const newBudget = req.body;

  // Read the JSON file
  fs.readFile('data.json', 'utf8', (err, data) => {
      if (err) {
          console.error(err);
          res.status(500).send('Error reading file');
          return;
      }

      let jsonData = JSON.parse(data);

      // Find the user
      const userIndex = jsonData.findIndex(item => item.email === email);
      if (userIndex === -1) {
          res.status(404).send('User not found');
          return;
      }

      // Update the budget
      jsonData[userIndex].monthlyBudget = newBudget.monthlyBudget;
      jsonData[userIndex].weeklyBudget = newBudget.weeklyBudget;
      jsonData[userIndex].budgetAlert = newBudget.budgetAlert;

      // Write the updated data back to file
      fs.writeFile('data.json', JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Budget updated successfully');
      });
  });
});

// Route to update data
app.put('/data', (req, res) => {
  try {
    const { body: newData } = req;
    fs.writeFileSync(filePath, JSON.stringify(newData, null, 2), 'utf8');
    res.json({ message: 'Data updated successfully' });
  } catch (error) {
    console.error('Error updating data:', error);
    res.status(500).json({ error: 'Failed to update data' });
  }
});

// Route to delete data
app.delete('/data/:key', (req, res) => {
  try {
    const { key } = req.params;
    let data = JSON.parse(fs.readFileSync(filePath, 'utf8'));
    if (data.hasOwnProperty(key)) {
      delete data[key];
      fs.writeFileSync(filePath, JSON.stringify(data, null, 2), 'utf8');
      res.json({ message: `Deleted ${key} from data` });
    } else {
      res.status(404).json({ error: `${key} not found in data` });
    }
  } catch (error) {
    console.error('Error deleting data:', error);
    res.status(500).json({ error: 'Failed to delete data' });
  }
});

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
