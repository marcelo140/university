#![feature(iterator_step_by)]

extern crate clap;
extern crate criterion_plot as plot;
extern crate petgraph;
extern crate rand;

mod graph;

use petgraph::graph::{Neighbors, NodeIndex};
use petgraph::{Graph, Undirected};

use clap::{App, Arg, SubCommand};
use graph::*;
use plot::prelude::*;
use rand::Rng;
use std::ops::IndexMut;
use std::str::FromStr;

const DEFAULT_NODES: usize = 20;
const AVG_REPETITIONS: usize = 100;
const STEPS: usize = 10;

fn get_reliability(graph: &Graph<usize, (), Undirected>) -> f64 {
    let nodes = graph.raw_nodes();
    let receivers = nodes.iter().filter(|n| n.weight >= 1).count();
    let total = nodes.len();

    (receivers as f64 / total as f64)
}

fn average_reliability_for(graph: &Graph<usize, (), Undirected>, fraction: f32) -> f64 {
    let mut acc = 0.0;

    for _ in 0..AVG_REPETITIONS {
        let bgraph = broadcast(&graph, fraction);
        acc += get_reliability(&bgraph);
    }

    (acc / (AVG_REPETITIONS as f64)) * 100.0
}

fn plot_broadcast(nodes: usize, gtype: &GraphType) {
    let xs: Vec<usize> = (10..nodes).step_by(STEPS).collect();
    let gs: Vec<Graph<usize, (), Undirected>> = xs.iter().map(|x| gtype.build(*x)).collect();

    Figure::new()
        .configure(Axis::BottomX, |a| {
            a.set(Label("Number of nodes"))
                .configure(Grid::Major, |g| g.show())
        })
        .configure(Axis::LeftY, |a| {
            a.set(Label("Reliability"))
                .configure(Grid::Major, |g| g.show())
        })
        .configure(Key, |k| {
            k.set(Position::Outside(Vertical::Top, Horizontal::Right))
                .set(Order::SampleText)
                .set(Title("Fraction"))
        })
        .plot(
            LinesPoints {
                x: &xs,
                y: gs.iter().map(|g| average_reliability_for(g, 0.20)),
            },
            |lp| {
                lp.set(PointType::FilledCircle)
                    .set(PointSize(0.6))
                    .set(Color::Cyan)
                    .set(Label("0.20"))
            },
        )
        .plot(
            LinesPoints {
                x: &xs,
                y: gs.iter().map(|g| average_reliability_for(g, 0.40)),
            },
            |lp| {
                lp.set(PointType::FilledCircle)
                    .set(PointSize(0.6))
                    .set(Color::ForestGreen)
                    .set(Label("0.40"))
            },
        )
        .plot(
            LinesPoints {
                x: &xs,
                y: gs.iter().map(|g| average_reliability_for(g, 0.60)),
            },
            |lp| {
                lp.set(PointType::FilledCircle)
                    .set(PointSize(0.6))
                    .set(Color::DarkViolet)
                    .set(Label("0.60"))
            },
        )
        .plot(
            LinesPoints {
                x: &xs,
                y: gs.iter().map(|g| average_reliability_for(g, 0.80)),
            },
            |lp| {
                lp.set(PointType::FilledCircle)
                    .set(PointSize(0.6))
                    .set(Color::Magenta)
                    .set(Label("0.80"))
            },
        )
        .plot(
            LinesPoints {
                x: &xs,
                y: gs.iter().map(|g| average_reliability_for(g, 1.00)),
            },
            |lp| {
                lp.set(PointType::FilledCircle)
                    .set(PointSize(0.6))
                    .set(Color::Red)
                    .set(Label("1.00"))
            },
        )
        .draw()
        .ok()
        .and_then(|gnuplot| {
            gnuplot
                .wait_with_output()
                .ok()
                .and_then(|p| String::from_utf8(p.stderr).ok())
        });
}

fn sample_neighbors(neighbors: Neighbors<()>, fraction: f32) -> Vec<NodeIndex> {
    let mut rnd = rand::thread_rng();

    let n = fraction * neighbors.clone().count() as f32;
    match rand::seq::sample_iter(&mut rnd, neighbors, n.ceil() as usize) {
        Ok(neighbors) => neighbors,
        Err(neighbors) => neighbors,
    }
}

fn broadcast(graph: &Graph<usize, (), Undirected>, fraction: f32) -> Graph<usize, (), Undirected> {
    let mut rand = rand::thread_rng();
    let mut graph = graph.map(|_, _| 0, |_, _| ());
    let mut to_visit = Vec::new();
    let mut hops = 0;
    let indices: Vec<NodeIndex> = graph.node_indices().collect();

    to_visit.push(*rand.choose(&indices).unwrap());

    while !to_visit.is_empty() {
        hops += 1;
        let mut neighbors = Vec::new();

        for idx in &to_visit {
            {
                let node = graph.index_mut(*idx);

                if *node >= 1 {
                    continue;
                }

                *node = hops;
            }
            let idx_neighbors = graph.neighbors(*idx);
            neighbors.extend(sample_neighbors(idx_neighbors, fraction));
        }
        to_visit = neighbors;
    }

    graph
}

fn main() {
    let app = App::new("probabilistic-broadcast")
        .about("Allows some sampling with probabilistic broadcast")
        .arg(
            Arg::with_name("Type")
                .help("Type of graph to generate")
                .default_value("connected"),
        )
        .arg(Arg::with_name("N").global(true).help("Number of nodes"))
        .subcommand(
            SubCommand::with_name("generate").arg(
                Arg::with_name("Fraction")
                    .help("Fraction of neighbors to be selected")
                    .default_value("1.0"),
            ),
        )
        .subcommand(SubCommand::with_name("plot"))
        .get_matches();

    let nodes = app
        .value_of("N")
        .and_then(|s| usize::from_str(s).ok())
        .unwrap_or(DEFAULT_NODES);

    let gtype = match app.value_of("Type") {
        Some("preferential") => GraphType::Preferential,
        _ => GraphType::Connected,
    };

    let fraction = match app.subcommand_matches("generate") {
        None => 1.0,
        Some(comm) => comm
            .value_of("Fraction")
            .and_then(|s| f32::from_str(s).ok())
            .unwrap(),
    };

    if app.is_present("plot") {
        plot_broadcast(nodes + 10, &gtype);
    } else {
        let graph = gtype.build(nodes);
        let graph = broadcast(&graph, fraction);
        print_graph(&graph);
    }
}
