import express from 'express';
import fs from 'fs';
import { fileURLToPath } from 'url';
import path from 'path';
import cors from 'cors';

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
      const jsonData = JSON.parse(data);
      
      // Assuming the user's email is provided in the request query parameter
      const userEmail = req.query.email;
      
      // Find the user with the provided email
      const user = jsonData.find(user => user.email === userEmail);
      if (!user) {
        res.status(404).json({ error: 'User not found' });
        return;
      }
      
      // Return only the user's data
      res.json(user);
    } catch (error) {
      console.error('Error reading data from file:', error);
      res.status(500).json({ error: 'Failed to read data' });
    }
  });

// Endpoint to create a new user
app.post('/createUser', (req, res) => {
    const newUser = {
        fullName: req.body.fullName,
        userName: req.body.userName,
        email: req.body.email,
        password: req.body.password,
        income: 0,
        monthlyBudget: 0,
        weeklyBudget: 0,
        budgetAlert: 90,
        categories: [],
        spendings: [],
        rewards: []
    };

    // Read the JSON file
    fs.readFile(filePath, 'utf8', (err, data) => {
        if (err) {
            console.error(err);
            res.status(500).send('Error reading file');
            return;
        }

        let jsonData = JSON.parse(data);

        // Check if the email already exists
        const existingUser = jsonData.find(item => item.email === newUser.email);
        if (existingUser) {
            res.status(400).send('User already exists');
            return;
        }

        // Add the new user
        jsonData.push(newUser);

        // Write the updated data back to file
        fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
            if (err) {
                console.error(err);
                res.status(500).json({message: 'Error writing file'});
                return;
            }
            res.status(200).send({message: 'User created successfully'});
        });
    });
});

// Endpoint to log in
app.post('/login', (req, res) => {
    const { email, password } = req.body;

    // Read the JSON file
    fs.readFile(filePath, 'utf8', (err, data) => {
        if (err) {
            console.error(err);
            res.status(500).send('Error reading file');
            return;
        }

        const jsonData = JSON.parse(data);

        // Check if the user exists and the password is correct
        const user = jsonData.find(item => item.email === email && item.password === password);
        if (user) {
            res.status(200).json({ message: 'Login successful'});
        } else {
            res.status(401).send({ message: 'Invalid email or password' });
        }
    });
});

app.put('/changeUserName/:email', (req, res) => {

    

    const email = req.params.email;
    const newUserName = req.body.newUserName;

    // Read the JSON file
    fs.readFile(filePath, 'utf8', (err, data) => {
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

        // Update the userName
        jsonData[userIndex].userName = newUserName;

        // Write the updated data back to file
        fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
            if (err) {
                console.error(err);
                res.status(500).send('Error writing file');
                return;
            }
            res.status(200).send('User name updated successfully');
        });
    });
});

app.post('/addCategory/:email', (req, res) => {
  const email = req.params.email;
  const newCategory = req.body;

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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

      // Add the new category
      jsonData[userIndex].categories.push(newCategory);

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Category added successfully');
      });
  });
});

app.delete('/deleteCategory/:email/:index', (req, res) => {
  const email = req.params.email;
  const index = parseInt(req.params.index); // Convert index to integer

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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
      if (index < 0 || index >= jsonData[userIndex].categories.length) {
          res.status(400).send('Invalid index');
          return;
      }

      // Get the category to be deleted
      const categoryToDelete = jsonData[userIndex].categories[index].name;

      // Remove all spendings with the same category
      jsonData[userIndex].spendings = jsonData[userIndex].spendings.filter(s => s.category !== categoryToDelete);

      // Remove the category
      jsonData[userIndex].categories.splice(index, 1);

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Category and associated spendings deleted successfully');
      });
  });
});




app.post('/addReward/:email', (req, res) => {
  const email = req.params.email;
  const newReward = req.body;

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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

      // Add the new reward
      jsonData[userIndex].rewards.push(newReward);

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Reward added successfully');
      });
  });
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

app.delete('/eraseRewards/:email', (req, res) => {
  const email = req.params.email;

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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

      // Erase all rewards for the user
      jsonData[userIndex].rewards = [];

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Rewards erased successfully');
      });
  });
});


app.post('/addSpending/:email', (req, res) => {
  const email = req.params.email;
  const newSpending = req.body;

  console.log(newSpending);

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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

      // Add the new spending
      jsonData[userIndex].spendings.push(newSpending);

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Spending added successfully');
      });
  });
});

app.delete('/deleteSpending/:email/:index', (req, res) => {
  const email = req.params.email;
  const index = parseInt(req.params.index); // Convert index to integer

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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
      if (index < 0 || index >= jsonData[userIndex].spendings.length) {
          res.status(400).send('Invalid index');
          return;
      }

      // Remove the spending
      jsonData[userIndex].spendings.splice(index, 1);

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Spending deleted successfully');
      });
  });
});

app.delete('/eraseCategories/:email', (req, res) => {
  const email = req.params.email;

  // Read the JSON file
  fs.readFile(filePath, 'utf8', (err, data) => {
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

      // Erase all categories and spendings for the user
      jsonData[userIndex].categories = [];
      jsonData[userIndex].spendings = [];

      // Write the updated data back to file
      fs.writeFile(filePath, JSON.stringify(jsonData, null, 2), 'utf8', (err) => {
          if (err) {
              console.error(err);
              res.status(500).send('Error writing file');
              return;
          }
          res.status(200).send('Data erased successfully');
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
